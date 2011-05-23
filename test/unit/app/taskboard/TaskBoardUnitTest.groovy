package app.taskboard

import grails.test.*
import groovy.time.TimeCategory


/**
 * Representation of a TaskBoard unit test class.
 * This mocks a complete domain that can be used by all subsequent unit tests.
 * 
 * @author Robert Krombholz
 *
 */
class TaskBoardUnitTest extends GrailsUnitTestCase {
	
	def taskService
	
    protected void setUp() {
        super.setUp()
		taskService = new TaskService()
		def springSecurityExpando = new Expando()
		//Mocking the security service (only principal as this is enough for our purposes
		springSecurityExpando.metaClass.getPrincipal = {return ['username':'testuser']}
		taskService.springSecurityService = springSecurityExpando
		assertEquals 'testuser', springSecurityExpando.principal.username
		
		mockConfig '''
		taskboard.colors = ['#faf77a', '#fa7a88', '#bcbcf5', '#f9d7a9']
		taskboard.priorities = ['Critical', 'Major', 'Normal', 'Low']
		// Added by the Spring Security Core plugin:
		grails.plugins.springsecurity.userLookup.userDomainClassName = 'app.taskboard.User'
		grails.plugins.springsecurity.userLookup.authorityJoinClassName = 'app.taskboard.UserRole'
		grails.plugins.springsecurity.authority.className = 'app.taskboard.Role'
		grails.plugins.springsecurity.securityConfigType = SecurityConfigType.Annotation
		'''
		mockDomain(User,[
			new User(
				username: 'testuser',
				password: 'testpassword',
				firstname: 'firstname',
				lastname: 'lastname',
				email: 'e@mail.com'
			)]
		)
		//No need to mock this with data currently
		mockDomain(Role)
		mockDomain(UserRole)
		
		//Board>Column/User relationship mocking
		def column1 = new Column(name:'todo')
		def column2 = new Column(name:'wip')
		def column3 = new Column(name:'done')
		def board = new Board(name:'myboard')
		def user = User.findByUsername('testuser')
		board.columns = [column1, column2, column3]
		board.users = [user]
		column1.board = board
		column2.board = board
		column3.board = board
		
		mockDomain(Column, [column1, column2, column3])
		mockDomain(Board, [board])
		

		//Mock tasks 				
		def todo = []		//
		for(i in 1..20) {
			todo << new Task(name: "testtask$i", durationHours: 0.5, column: column1,
				creator: user, sortorder: i, color: '#faf77a', priority: 'Critical'
			)			
		}
		mockDomain(Task, todo)
		mockDomain(TaskMovementEvent)
		mockDomain(ColumnStatusEntry)
		
		//Re-setting the todo as we need the IDs generated by hibernate (or the mock)
		todo = Task.list()
		//This is the simulated order of the target column
		def orderedTaskIdListWip = []
		def orderedTaskIdListDone = []
		//Moving tasks over the whole board. 1 Per day over both columns too the end
		todo.eachWithIndex { task, idx->
			//We just put everything in the lists - we don't care about gaps here when tasks are moved
			orderedTaskIdListWip << task.id
			orderedTaskIdListDone << task.id
			// starting from my birthday :P
			Date startDate = Date.parse("dd/MM/yyyy", "02/04/2011")
			//Need to be authenticated for that			
			use(TimeCategory) {
				taskService.moveTask orderedTaskIdListWip, Column.findByName('todo').id, Column.findByName('wip').id, task.id, startDate-idx.days
				taskService.moveTask orderedTaskIdListDone, Column.findByName('wip').id, Column.findByName('done').id, task.id, startDate-idx.days
			}								
		}							
    }

    protected void tearDown() {
        super.tearDown()
    }
	
	void testOwnMockup() {
		assertEquals 40, TaskMovementEvent.list().size()
	}
}
