package app.taskboard

/**
 * Representing an immutable message that contains all information about a TaskMovement.
 * 
 * @author rkrombho
 *
 */
@Immutable
class TaskMovementMessage {
	//IDs
	int task
	int fromColumn
	int toColumn
	List newTaskOrderIdList
}
