package app.taskboard

/**
 * Represents a general exception within the taskboard app.
 * 
 * @author Robert Krombholz
 *
 */
class TaskBoardException extends RuntimeException {

	public TaskBoardException(Board board, String message) {
		super("Board #${board.id} [${board.name}] - $message")
	}
}
