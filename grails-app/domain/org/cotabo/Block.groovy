package org.cotabo

import java.util.Date;

/**
 * Represents a blocked state of a Task.
 * We persist that to do reporting of that later on.
 * 
 * @author Robert Krombholz
 *
 */
class Block {
	static belongsTo = [Task]
	//Will be setted automatically - when the blocked situation occurs
	Date dateCreated
	//When the blocked situation is resolved.
	Date dateClosed

    static constraints = {		
		dateClosed nullable:true
    }
	
	static exportables = ['dateCreated', 'dateClosed']
}
