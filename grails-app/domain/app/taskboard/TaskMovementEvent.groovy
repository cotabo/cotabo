package app.taskboard

/**
 * This domain class represents the event when a task is moved
 * from one column to another.
 * 
 * This is a non-modifiable object.
 * 
 * @author Robert Krombholz
 *
 */
class TaskMovementEvent {
	//The task that this event applies too
	Task task
	//From which column it was moved
	Column fromColumn
	//Too which column it was moved
	Column tooColumn
	//The user who moved it
	User user
	
	//When this event occured
	Date dateCreated
	
	
    static constraints = {
		task nullable:false
		//In case of the first column
		fromColumn nullable:true
		tooColumn nullable:false
		user nullable:false
    }

	
	/**
	 * We want to set the workflowEndDate when we generate the events
	 * in case that the target column is the last one on the board.
	 *
	 * We also set the workflowStartDate when the targetColumn for this event
	 * is the first one in the workflow (this can be user-defined on board-creation)
	 */
	def afterInsert = {
		def lastColumnOnBoard = tooColumn.board.columns.last()
		if(tooColumn.id == lastColumnOnBoard.id) {
			task.workflowEndDate = dateCreated
		}
		else if (tooColumn.workflowStartColumn) {
			task.workflowStartDate = dateCreated
		}
		task.save()
	}
}
