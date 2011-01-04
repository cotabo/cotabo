package app.taskboard

import grails.test.*

class TaskIntegrationTests extends GrailsUnitTestCase {
    protected void setUp() {
        super.setUp()
		//Preperation
		def user = new User(
				username: 'testuser',
				password: 'testpassword',
				firstname: 'firstname',
				lastname: 'lastname',
				email: 'e@mail.com'
		).save()
		def column = new Column(name:'mycolumn')						
		def board = new Board(name:'myboard')
			.addToUsers(user)
			.addToColumns(column)
		
		board.save()		
		new Color(colorCode:'#FFFFFF').save()
		new Priority(name:'critital').save()
    }

    protected void tearDown() {
        super.tearDown()
    }

	
	void testCreationAddToColumn() {
		//Preperation
		def col = Column.findByName('mycolumn')
		def user = User.findByUsername('testuser')
		def color = Color.findByColorCode('#FFFFFF')
		def prio = Priority.findByName('critital')
		
		assertNotNull col
		assertNotNull user
		assertNotNull color
		assertNotNull prio
		
		def newCol =col.addToTasks(
			name: 'mytask',
			durationHours: 0.5,
			color: color,
			priority: prio,
			creator: user
		)
		assertTrue newCol.validate()
		assertNotNull newCol.save()
		
		def tmpTask = Task.findByName('mytask')
		assertNotNull tmpTask
		assertEquals 'mycolumn', tmpTask.column.name
	}
}
