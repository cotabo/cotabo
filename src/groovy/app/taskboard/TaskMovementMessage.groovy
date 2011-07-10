package app.taskboard

/**
 * Representing an immutable message that contains all information about a TaskMovement.
 * 
 * @author Robert Krombholz
 *
 */
class TaskMovementMessage {
	//IDs
	int task
	int fromColumn
	int toColumn
	List newTaskOrderIdList
}
