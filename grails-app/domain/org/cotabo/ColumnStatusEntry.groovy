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
	static belongsTo = Column
	Column column	
	int tasks = 0
	
	//Grails tracked creation date
	Date dateCreated

	def beforeUpdate() {
		//Throw an exception when someone tries to update this
		throw new EventUpdateException(this.task)
	}
}
