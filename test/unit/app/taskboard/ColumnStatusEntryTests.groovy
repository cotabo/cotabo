package app.taskboard

import grails.test.*

class ColumnStatusEntryTests extends GrailsUnitTestCase {
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
		mockDomain(Task)
		column.addToTasks([
			new Task(
				name: 'mytask',
				durationHours: 0.5,
				column: column,
				creator: user,
				sortorder: 1,
				color: '#faf77a',
				priority: 'Critical'
		),
			new Task(
				name: 'mytask2',
				durationHours: 0.5,
				column: column,
				creator: user,
				sortorder: 1,
				color: '#faf77a',
				priority: 'Critical'
			)
		]);
		column.save()
		mockDomain(TaskMovementEvent)
		mockDomain(ColumnStatusEntry)
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testCreation() {
		def col = Column.findByName('mycolumn')	
		assertNotNull col
		assertNotNull col.tasks	
		def entry = new ColumnStatusEntry(column:col, tasks:col.tasks)
		
		assertTrue entry.validate()
		assertNotNull entry.save()
		assertEquals col.tasks, entry.tasks
    }
	
}
