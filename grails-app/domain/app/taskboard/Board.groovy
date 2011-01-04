package app.taskboard

import java.util.Date;
import java.util.List;

/**
 * Represents a Board Object consisting of Columns.
 * It has relationship to User to maintain the Users who are allowed to work on this Board.
 * 
 * @author Robert Krobmholz
 *
 */
class Board {
	//Relationships
	List columns
	List users	
	List admins	
	static hasMany = [ columns : Column, users : User, admins: User  ]
	static mappedBy = [users: "userBoards", admins: "adminBoards"]
	
	String name
	String description
		
	
	//Automatically maintained
	Date dateCreated

    static constraints = {
		name blank:false, maxSize:25, unique: true
		description nullable:true, blank:true, maxSize:254
		columns nullable:false, maxSize:10
		users nullable:false
		admins nullable:false, maxSize: 5
    }
	
	@Override
	public String toString() {
		return name
	}
}
