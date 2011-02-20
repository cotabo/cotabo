package app.taskboard

/**
* Represents the priority of a Task object
*
* @author Robert Krombholz
*
*/
class Priority {
	
	static belongsTo = Board
	
	String name

    static constraints = {
		name nullable:false, unique:true
    }
	
	@Override
	public String toString() {
		return name
	}
}
