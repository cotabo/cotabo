package org.cotabo

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
	int tasks = 0
	//A flag whether this is an entry for the entrance or the leave of a task
	boolean entered 
	
	//Grails tracked creation date
	Date dateCreated

    static constraints = {
		column nullable:false		
    }
	
	def beforeUpdate() {
		//Throw an exception when someone tries to update this
		throw new EventUpdateException(this.task)
	}
}
