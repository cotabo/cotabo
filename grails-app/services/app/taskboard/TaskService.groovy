package app.taskboard

import java.util.ArrayList;

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
	 * @param taskInstance with all necessary properties set.
	 * @return a task object. Either persistest or with errors populated (use hasErrors()).
	 */
	Task saveTask(Task taskInstance, Date dateCreated = null) {	
		if(taskInstance.validate()) {
			taskInstance.column.addToTasks(taskInstance).save()
			//saving both as addTo somehow here doesn't seem to main both ends of the relation
			taskInstance.save()	
			createMovementEvent(taskInstance, null, taskInstance.column, dateCreated)
		}
		return taskInstance
	}

	/**
	 * Updates the sort order of all tasks at the db level as per their
	 * correspinding list order.
	 * 
	 * @param sortedNewTaskIdsTargetColumn an ordered list of Task IDs
	 * @return a message which is empty then the update was successfull.
	 */
    String updateSortOrder(List sortedNewTaskIdsTargetColumn) {
		try {			
			updateTaskOrder(sortedNewTaskIdsTargetColumn)
			sessionFactory?.getCurrentSession()?.flush()
			return ''
		}
		catch (Exception e) {
			return 'Error occured while persisting the data. Please contact the system administrator'
		}

    }

	/**
	 * Updates the sort order of the target column & moves the given task
	 *  
	 * @param sortedNewTaskIdsTargetColumn a list of IDs with the new sort order of the target column 
	 * @param fromColumnId the column id from where a task is moved
	 * @param tooColumnId the column id where the task is moved too
	 * @param taskId the task that should be moved
	 * @param dateCreated Optional - this is only for testing purposes - normally hibernate/grails will set this.
	 * @return a message which is empty then the update was successfull.
	 */
	String moveTask(def message, Date dateCreated = null) {						
		
		updateTaskOrder(message.newTaskOrderIdList)		
		def fromColumnInstance = Column.get(message.fromColumn)
		def toColumnInstance = Column.get(message.toColumn)
		def taskInstance = Task.get(message.task)		
		if (fromColumnInstance && toColumnInstance && taskInstance) {
			fromColumnInstance.removeFromTasks(taskInstance)
			toColumnInstance.addToTasks(taskInstance)
			fromColumnInstance.save()
			toColumnInstance.save()			
			sessionFactory?.getCurrentSession()?.flush()
			createMovementEvent(taskInstance, fromColumnInstance, toColumnInstance, dateCreated)
			return ''
		}
		else {
			return 'Either the column or the Task that you have specified does not exist.'
		}
						
	}
			
	/**
	 *  Private method that creates & persists all events when a task is moved.
	 * 	<b>Note:</b> this should be called after the movement is done.
	 * 
	 * @param task The task that was transfered.
	 * @param fromColumn The column object from which the task was transfered.
	 * @param tooColumn The column object to which the task was transfered.
	 * @param dateCreated Optional - this is only for testing purposes - normally hibernate/grails will set this.
	 */
	private void createMovementEvent(Task task, Column fromColumn, Column tooColumn, Date dateCreated = null) {		
		def events = []
		//Get the current logged-in user
		def principal = springSecurityService.principal
		def user = User.findByUsername(principal.username)
		
		//We create 1 task Movement event ...
		events << new TaskMovementEvent(
			task: task, 
			fromColumn: fromColumn,
			tooColumn: tooColumn,
			user: user,
			dateCreated: dateCreated)		
		//We only add the 1st column if not empty (maybe empty when new task is created)
		if (fromColumn) {
			//Might be zero in terms of task creation
			events << new ColumnStatusEntry(
				column: fromColumn,
				//Just saving the number of tasks that after the movement
				tasks: fromColumn.tasks?.size() ?: 0,
				entered: false,
				dateCreated: dateCreated
			)			
		}		
		//And another column status entry for the target (this always exists)
		events << new ColumnStatusEntry(
			column: tooColumn,
			//Just saving the number of tasks that after the movement
			//Need to handle the case where the target column is empty
			tasks: tooColumn.tasks?.size() ?: 0,
			entered: true,
			dateCreated: dateCreated
		)		
		//Save everything		
		events.each {it.save(flush:false)}
		sessionFactory?.getCurrentSession()?.flush()
	}
	
	/**
	 * Updates the sortorder ofall tasks of the given tasksIdList as they are 
	 * ordered in the List.
	 * 
	 * @param orderedIdList
	 */
	private void updateTaskOrder(List orderedIdList) {
		
		if(orderedIdList) {
			//Itterate over all tasks in the column where the task was added
			orderedIdList.eachWithIndex { obj, idx ->
				def tmpTask = Task.get(obj)
				//Set the current iteration index as the sort order to maintain
				//as the user sees it.
				tmpTask.sortorder = idx				
				tmpTask.save(flush:false)
			}
			sessionFactory?.getCurrentSession()?.flush()
		}
	}
}
