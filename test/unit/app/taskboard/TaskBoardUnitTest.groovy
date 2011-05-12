package app.taskboard

import grails.test.ControllerUnitTestCase
import groovy.time.TimeCategory

/**
 * Representation of a TaskBoard unit test class.
 * This mocks a complete domain that can be used by all subsequent unit tests.
 * 
 * @author Robert Krombholz
 *
 */
class TaskBoardUnitTest extends ControllerUnitTestCase {
	
	    protected void setUp() {
        super.setUp()
		mockConfig '''
		taskboard.colors = ['#faf77a', '#fa7a88', '#bcbcf5', '#f9d7a9']
		taskboard.priorities = ['Critical', 'Major', 'Normal', 'Low']
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
		
		//Board>Column/User relationship mocking
		def column1 = new Column(name:'todo')
		def column2 = new Column(name:'wip')
		def column3 = new Column(name:'done')
		def board = new Board(name:'myboard')
		def user = User.findByUsername('testuser')
		board.columns = [column1, column2, column3]
		board.users = [user]
		column.board = board
		
		mockDomain(Column, [column1, column2, column3])
		mockDomain(Board, [board])
		

		def todo = []
		//Tasks
		for(i in 1..20) {
			todo << new Task(name: "testtask$i", durationHours: 0.5, column: column1,
				creator: user, sortorder: i, color: '#faf77a', priority: 'Critical'
			)			
		}
		def wip = []
		for(i in 21..30) {
			wip << new Task(name: "testtask$i", durationHours: 0.5, column: column2,
				creator: user, sortorder: i, color: '#faf77a', priority: 'Critical'
			)
		}
		def done = []
		for(i in 31..40) {
			done << new Task(name: "testtask$i", durationHours: 0.5, column: column3,
				creator: user, sortorder: i, color: '#faf77a', priority: 'Critical'
			)
		}
		mockDomain(Task, todo + wip + done)
		
		def taskMovementEvents = []
		def columnStatusEntries = []
		def myuser = User.findByUsername('testuser')
		Date startDate = Date.parse("dd/MM/yyyy", "02/04/2011")
		//TODO: generate event Data

		
							
    }

    protected void tearDown() {
        super.tearDown()
    }
}
