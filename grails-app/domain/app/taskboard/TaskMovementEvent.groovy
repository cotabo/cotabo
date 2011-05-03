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
final class TaskMovementEvent {
	//The task that this event applies too
	Task task
	//From which column it was moved
	Column fromColumn
	//Too which column it was moved
	Column tooColumn
	
	//When this event occured
	Date dateCreated
	
	
    static constraints = {
		task nullable:false
		//In case of the first column
		fromColumn nullable:true
		tooColumn nullable:false
    }			
	
	def beforeUpdate() {
		//Throw an exception when someone tries to update this
		throw new EventUpdateException(this.task)
	}
}
