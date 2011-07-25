package org.cotabo
import org.codehaus.groovy.grails.commons.ConfigurationHolder as grailsConfig

/**
 * Represents a Task object on a Taskboard column.
 * It can only exist on a Column and only in one Column at a time.
 * 
 * @author Robert Krombholz
 *
 */
class Task implements Comparable {

	//This is determined at runtime by the related blocks
	static transients = ["blocked"]
	
	//Relationships
	static belongsTo = [ column : Column ]	
	//Blocked states
	static hasMany = [ blocks : Block ]
	List blocks = []
	
	User creator
	User assignee
	
	int sortorder 
	Date dateCreated
	Date lastUpdated
	
	Date workflowStartDate
	Date workflowEndDate
	
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
		workflowStartDate nullable: true
		workflowEndDate nullable:true
    }
	
	@Override
	public String toString() {
		return name
	}
	
	int compareTo(obj) {
		sortorder <=> obj?.sortorder
	}
	
	static mapping = {
		//column is a reserver word in MySQL
		column column:'cotabo_column'
		sort sortorder:'asc'		
	}
	
	def toMessage() {		
		return [
			'id':this.id,
			'creator':"${creator.encodeAsHTML()}",
			'assignee':"${assignee.encodeAsHTML()}",
			'sortorder':sortorder.encodeAsHTML(),
			'dateCreated':dateCreated.encodeAsHTML(),						
			'workflowStartDate':workflowStartDate.encodeAsHTML(),
			'workflowEndDate':workflowEndDate.encodeAsHTML(),			
			'name':name.encodeAsHTML(),
			'description':description.encodeAsHTML(),
			'durationHours':durationHours.encodeAsHTML(),
			'priority':priority.encodeAsHTML(),
			'color':color.encodeAsHTML()
		]
	}
	
   /**
	* Marking this task as blocked or resolves the blocked state.
	* 
	* @param blocked true means setting this task to blocked. false means resolving the blocked situation
	*/
   void setBlocked(boolean blocked) {
	   def block = this.blocks.find{!it.dateClosed}
	   //Potentially finding an open Block	   
	   if (blocked) {
		   if (block) {
			   //Do nothing when someone wants to add a block
			   //if there is still an open block for this task
			   return
		   }
		   //While creating new objects - we need to to that within a different session		   
		   Block.withNewSession {
			   this.addToBlocks(new Block())
		   }
	   }
	   else {		   		   
		   if(block) {				   
			   block.dateClosed = new Date()
			   block.save()
		   }
	   }
   }
   
   /**
    * Checks whether this task is blocked.
    * @return true if it is blocked
    */
   boolean isBlocked() {
	   //Potentially finding the block
	   def block = this.blocks.find{it.dateClosed == null}
	   block ? true : false
   }

   /**
    * Workaround as sometimes the get... is called instead of is...
    * while reading the property
    * @return
    */
   boolean getBlocked() {
	   return this.isBlocked()
   }
}
