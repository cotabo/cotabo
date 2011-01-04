package app.taskboard

/**
 * Represents a Task object on a Taskboard column.
 * It can only exist on a Column and only in one Column at a time.
 * 
 * @author Robert Krombholz
 *
 */
class Task {
	//Relationships
	static belongsTo = [ column : Column ]
	User creator
	User assignee
	
	String name
	String description
	double durationHours
	Color color
	Priority priority
	
    static constraints = {
		name blank:false, maxSize:100
		description nullable:true, blank:true, maxSize:254
		durationHours nullable:false, min:0D, max:500D		
		color nullable:false
		priority nullable:false
		column nullable:false
		creator nullable:false
		assignee nullable:true
    }
	
	@Override
	public String toString() {
		return name
	}
}
