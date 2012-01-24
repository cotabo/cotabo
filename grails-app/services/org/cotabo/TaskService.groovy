package org.cotabo

import java.util.ArrayList;
import grails.util.Environment
import groovy.time.TimeCategory

/**
 * Task service class offering everything necessary for tasks related activities.
 * 
 * @author Robert Krombholz
 *
 */
class TaskService {

    static transactional = true
	def sessionFactory
	def springSecurityService

	/**
	 * This method persists task object and generates the necessary events
	 * from the request parameters.
	 * 
	 * @param task that passes .validate()
	 * @return a task object. Either persistest or with errors populated (use hasErrors()).
	 */
	Task saveTask(Task task, Date dateCreated = null) {	
		if(task.validate()) {
			//For test purposes
			task.dateCreated = dateCreated
			def col = Column.get(task.column.id)
			col.addToTasks(task)
			col.save()
			//Also need to save the task here.
			task.save()
			createMovementEvent(task, dateCreated)
		}
		return task
	}

	/**
	 * Updates the sort order of the target column & moves the given task
	 *  	 
	 * @param fromColumn the column from where a task is moved
	 * @param tooColumn the column where the task is moved too
	 * @param task the task that should be moved
	 * @param position Optional - specify the 0 based index for the target column
	 * @param dateCreated Optional - this is only for testing purposes - normally hibernate/grails will set this.
	 * @return a message 	which is empty then the update was successfull.
	 */
	void moveTask(Column fromColumn, Column toColumn, Task task, int position = -1, Date dateCreated = null) {
		if (fromColumn && toColumn && task) {
			fromColumn.removeFromTasks(task)
			toColumn.addToTasks(task)
			
			// Checking for last column in workflow
			if(toColumn.workflowEndColumn) {
				def date
				use(TimeCategory) {
					//For testing we always move +4 hours later than created
					date = Environment.current == Environment.TEST ? task.startDate + 4.hours : new Date()
				}
				task.workflowEndDate = date
			}
			else if (toColumn.workflowStartColumn) {
				//Set the date to out defined startDate if environment is testTaskBoardUnitTest.startDate
				def date = Environment.current == Environment.TEST ? task.startDate : new Date()
				task.workflowStartDate = date
				task.workflowEndDate = null
			}
			else {
				task.workflowEndDate = null
			}
			//Making the user who pulled the task - the assignee
			task.assignee = User.findByUsername(springSecurityService.principal.username)
			createMovementEvent(task, dateCreated)
			task.save()	
		}
	}
	
	/**
	 * Sets the specified tasks onto the specified position.
	 * 
	 * @param task The task that should be reordered
	 * @param position the 0 based index position to set the task too
	 */
	void reorderTask(Task task, int position) {
		def col = task.column
		col.removeFromTasks(task)
		col.tasks.add(position, task)	
		//Need to explicitly re set the column on the task as
		//the .add method only maintains one side of the relation
		task.column = col
		task.save()
	}
			
	/**
	 *  Private method that creates & persists all events when a task is moved.
	 * 	<b>Note:</b> this should be called after the movement is done.
	 * 
	 * @param task The task that was transfered.
	 * @param dateCreated Optional - this is only for testing purposes - normally hibernate/grails will set this.
	 */
	private void createMovementEvent(Task task, Date dateCreated = null) {		
		def events = []
		//Get the current logged-in user
		def principal = springSecurityService.principal
		def user = User.findByUsername(principal.username)
		
		//Getting a snapshot of task count on each column
		task.column.board.columns.each {
			events << new ColumnStatusEntry(
				column: it,
				tasks: it.tasks?.size() ?: 0,
				dateCreated: dateCreated
			)
		}
		//Save everything		
		events.each {it.save(flush:false)}
		sessionFactory?.getCurrentSession()?.flush()
	}	
}
