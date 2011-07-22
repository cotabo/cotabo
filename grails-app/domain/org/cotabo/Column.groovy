package org.cotabo

/**
 * Represents a column inside a defined Taskboard.
 * 
 * @author Robert Krombholz
 *
 */
class Column {
	//Relationships
	static belongsTo = [ board : Board ]
	SortedSet tasks
	static hasMany = [ tasks : Task ]
	
	String name
	String description
	int limit = 0
	
	boolean workflowStartColumn = false

    static constraints = {
		name blank:false, maxSize: 25
		limit min:0, max:75
		description nullable:true, blank:true, maxSize:254
		tasks nullable:true
		board nullable:false
    }
	
	@Override
	public String toString() {
		return name
	}
	static mapping = {
		tasks sort:'sortorder'

	}

}