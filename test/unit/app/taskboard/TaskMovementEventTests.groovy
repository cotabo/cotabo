package app.taskboard

import grails.test.*

class TaskMovementEventTests extends GrailsUnitTestCase {
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
		def column = new Column(name:'mycolumn')
		def column2 = new Column(name:'mycolumn2')
		def board = new Board(name:'myboard')
		def user = User.findByUsername('testuser')		
		board.columns = [column]
		board.users = [user]
		column.board = board
		

		mockDomain(Column, [column, column2])
		mockDomain(Board, [board])
		mockDomain(Task, [new Task(
			name: 'mytask',
			durationHours: 0.5,
			column: column,
			creator: user,
			sortorder: 1,
			color: '#faf77a',
			priority: 'Critical'
		)])
		mockDomain(TaskMovementEvent)		
    }

    protected void tearDown() {   
        super.tearDown()
    }

    void testEventCreation() {
		def col1 = Column.findByName('mycolumn')
		def col2 = Column.findByName('mycolumn2')
		def task = Task.findByName('mytask')
		
		def event = new TaskMovementEvent(task: task, fromColumn: col1, tooColumn: col2, user: User.findByUsername('testuser'))
		assertTrue event.validate()
		assertNotNull event.save()
		assertNotNull event.dateCreated
		
    }
	
	void failOnUpdate() {
		def col1 = Column.findByName('mycolumn')
		def col2 = Column.findByName('mycolumn2')
		def task = Task.findByName('mytask')
		
		def event = new TaskMovementEvent(task: task, fromColumn: col1, tooColumn: col2, user: User.findByUsername('testuser'))
		assertTrue event.validate()
		assertNotNull event.save()
		
		try{
			event.dateCreated = new Date()
			event.save()
		}
		catch(Exception e) {
			assertEquals 'EventUpdateException', e.class.name
		}
		
	}
}
