package org.cotabo

/**
 * Represents an exception for the case that a non-modifiable event objects
 * will be modified. 
 * 
 * @author Robert Krombholz
 *
 */
class EventUpdateException extends TaskBoardException {
	
	public EventUpdateException(Task task) {
		super(task.column?.board, "Someone tried to update a read-only event for the task #${task.id} [${task.name}].")
		
	}
	

}
