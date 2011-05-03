package app.taskboard
import org.codehaus.groovy.grails.commons.ConfigurationHolder as grailsConfig

/**
 * Represents a Task object on a Taskboard column.
 * It can only exist on a Column and only in one Column at a time.
 * 
 * @author Robert Krombholz
 *
 */
class Task implements Comparable {

	//Relationships
	static belongsTo = [ column : Column ]
	User creator
	User assignee
	
	int sortorder 
	Date dateCreated
	Date lastUpdated
	
	String name
	String description
	double durationHours
	String priority
	String color	
	
    static constraints = {
		name blank:false, maxSize:100
		description nullable:true, blank:true, maxSize:254
		durationHours nullable:false, min:0D, max:500D						
		column nullable:false
		creator nullable:false
		assignee nullable:true
		sortorder nullable:false, min:0
		priority nullable:false, validator: {val, obj -> val in grailsConfig.config.taskboard.priorities }
		color nullable:false, validator: {val, obj -> val in grailsConfig.config.taskboard.colors }
    }
	
	@Override
	public String toString() {
		return name
	}
	
	int compareTo(obj) {
		sortorder <=> obj?.sortorder
	}
	
	static mapping = {
		sort sortorder:'asc'		
	}

}
