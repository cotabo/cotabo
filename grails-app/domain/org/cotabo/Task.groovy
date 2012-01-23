package org.cotabo
import org.codehaus.groovy.grails.commons.ConfigurationHolder as grailsConfig
import grails.util.Environment
import groovy.time.TimeCategory

/**
 * Represents a Task object on a Taskboard column.
 * It can only exist on a Column and only in one Column at a time.
 * 
 * @author Robert Krombholz
 *
 */
class Task implements Rerenderable {

	//This is determined at runtime by the related blocks
	static transients = ["blocked", "startDate", "rerenderAction", "deadline"]
	
	//Relationships
	static belongsTo = [ column : Column ]	
	//Blocked states
	static hasMany = [ blocks : Block, colors: TaskColor]
	
	List blocks = []
	
	User creator
	User assignee
		
	Date dateCreated
	Date lastUpdated
	Date due
	
	Date workflowStartDate
	Date workflowEndDate
	
	String name
	String description
	String details
	double durationHours
	String priority
	
	boolean archived
	
	//Out startDate for testing purposes - see beforeUpdate and beforeInsert
	private Date startDate = Date.parse("dd/MM/yyyy HH:mm:ss SSS z", "02/04/2011 13:13:13 013 GMT+2:00")
	
    static constraints = {
		name blank:false, maxSize:100
		description nullable:true, blank:true, maxSize:254
		details nullable:true, blank:true
		durationHours nullable:false, min:0D, max:500D						
		column nullable:false
		creator nullable:false
		assignee nullable:true		
		priority nullable:false, validator: {val, obj -> val in grailsConfig.config.taskboard.priorities }
		workflowStartDate nullable: true
		workflowEndDate nullable:true
		dateCreated nullable:true
		due nullable:true
		
    }
	
	static exportables = ['name', 'description', 'details', 'priority', 'colors', 'creator', 'assignee', 'archived', 'blocks', 'workflowStartDate', 'workflowEndDate', 'due']
	
	@Override
	public String toString() {
		return name
	}

	static mapping = {
		//column is a reserver word in MySQL
		column column:'cotabo_column'				
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
   
   boolean isDeadline(){
	   def now = new Date()
	   if (due && !workflowEndDate) {
		   def diff = due.minus(grailsConfig.config.taskboard.default.deadline).minus(now)
		   return (diff <= 0) ? true : false
	   }
	   return  false
   }

   /**
    * Workaround as sometimes the get... is called instead of is...
    * while reading the property
    * @return
    */
   boolean getBlocked() {
	   return this.isBlocked()
   }
   
   /**
    * Hibernate event trigger to check whether this update
    * trigger the task to start or end the workflow (eg enters first column or enters last column)
    */
   def beforeUpdate = {	   
	   def lastColumnOnBoard = this.column.board.columns.last()	  
	   //If the target column is the last
	   if(this.column.id == lastColumnOnBoard.id) {
		   def date
		   use(TimeCategory) {
			   //For testing we always move +4 hours later than created
			   date = Environment.current == Environment.TEST ? startDate + 4.hours : new Date()
		   }		   
		   this.workflowEndDate = date		   
	   }
	   //if the target column is marked as workflowStartColumn	   
	   else if (this.column.workflowStartColumn) {
		   //Set the date to out defined startDate if environment is testTaskBoardUnitTest.startDate
		   def date = Environment.current == Environment.TEST ? startDate : new Date()
		   this.workflowStartDate = date
	   }
   }
   
   /**
    * Hibernate event trigger to check whether the first column (where the task is inserted) is
    * the workflowStartColumn - if yes, set the workflowStartDate
    */
   def beforeInsert = {
	   //Set the date to out defined startDate if environment is testTaskBoardUnitTest.startDate
	   def date = Environment.current == Environment.TEST ? startDate : new Date()
	   if(this.column.workflowStartColumn) {		   
		   this.workflowStartDate = date
	   }	   
   }
   
   /**
    * Implementation of Rerenderable. see @link org.cotabo.Rerenderable
    */
   public String getRerenderAction() {
	   return 'showDom'
   }
}
