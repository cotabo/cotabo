package org.cotabo

import java.util.Date;
import java.util.List;

/**
 * Represents a Board Object consisting of Columns.
 * 
 * @author Robert Krobmholz
 *
 */
class Board {
	//Relationships
	List columns	
	static hasMany = [ columns : Column ]	
	
	String name
	String description
		
	
	//Automatically maintained
	Date dateCreated

    static constraints = {
		name blank:false, maxSize:25, unique: true
		description nullable:true, blank:true, maxSize:254
		columns nullable:false, maxSize:10			
    }
	
	/**
	* Returns all related user of this board.
	* See role param below on returning behaviour
	* @param role -<i>optional</i> RoleEnum value. If given returning only the related board
	* with this role. Otherwhise returning all related boards.
	*
	* @return The related users based on the given role (or all if no role given)
	*/
	Set<User> getUsers(RoleEnum role = null) {
		if(role) {
			return UserBoard.findAllByBoardAndRole(this,role).collect { it.user } as Set
		}
		else {
			return UserBoard.findAllByBoard(this).collect { it.user } as Set
		}
	}
	
	@Override
	public String toString() {
		return name
	}
}
