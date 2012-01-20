package org.cotabo

import java.util.Date
import java.util.List

/**
 * Represents a Board Object consisting of Columns.
 * 
 * @author Robert Krobmholz
 *
 */
class Board implements Rerenderable {
	//Relationships
	List columns	
	static hasMany = [ columns : Column, colors: TaskColor ]	
	
	static exportables = ['name', 'description', 'columns']
	
	static transients = ["rerenderAction"]
	
	String name
	String description
	
	//Automatically maintained
	Date dateCreated

    static constraints = {
		name blank:false, maxSize:25, unique: true
		description nullable:true, blank:true, maxSize:254
		columns nullable:false, maxSize:10			
    }
	
	static mapping = {
		colors cascade:"delete"
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
	
	/**
	 * Create virtual columns containing archived tasks.
	 * @return
	 */
	def getArchivedcolumns () {
		def columns = []
		def now = new Date()
		def today = now - 1
		def week = now - 7
		def month = now - 31
		def year = now - 365
		def bc = Date.parse("yyyy-MM-dd", "1970-01-01")
		
		def times = [bc, year, month, week, today, now]
		def names = ["1970", "Year", "Month", "Week", "Today"]
		
		for(i in 0..times.size-2) {
			def name = names[i]
			def from = times[i]
			def to = times[i+1]
			def tasks = Task.findAllByArchivedAndWorkflowEndDateBetween(true, from, to)
			def column = [tasks : tasks, name:name, board : this] as Column
			columns << column
		}
		
		return columns
	}
	
	@Override
	public String toString() {
		return name
	}
	
   /**
    * Implementation of Rerenderable. see @link org.cotabo.Rerenderable
    */
   public String getRerenderAction() {
	   return 'showDom'
   }
}
