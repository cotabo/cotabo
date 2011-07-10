package app.taskboard

/**
 * Representing a message that contains all information about a TaskMovement.
 * 
 * @author rkrombho
 *
 */
class TaskMovementMessage {
	//IDs
	int task
	int fromColumn
	int toColumn
	List newTaskOrderIdList
}
