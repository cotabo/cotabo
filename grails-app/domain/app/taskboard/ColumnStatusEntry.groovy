package app.taskboard

/**
 * This domain object represents a column at a given point in time.
 * An entry should be created whenever a column state (in terms 
 * of contained tasks)changes.
 * 
 * This is a non-modifiable object.
 * 
 * @author Robert Krombholz
 * 
 */
class ColumnStatusEntry {	
	
	Column column
	SortedSet tasks
	static hasMany = [ tasks : Task ]
	
	//Grails tracked creation date
	Date dateCreated

    static constraints = {
		column nullable:false
		tasks nullable:false
    }
	
	def beforeUpdate() {
		//Throw an exception when someone tries to update this
		throw new EventUpdateException(this.task)
	}
}
