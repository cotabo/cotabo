package app.taskboard

import grails.test.*

class TaskTests extends GrailsUnitTestCase {
    protected void setUp() {
        super.setUp()
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
		def board = new Board(name:'myboard')
		def user = User.findByUsername('testuser')		
		board.columns = [column]
		board.users = [user]
		column.board = board
		
		mockDomain(Task)
		mockDomain(Column, [column])
		mockDomain(Board, [board])
		mockDomain(Color, [new Color(colorCode:'#FFFFFF')])
		mockDomain(Priority, [new Priority(name:'critital')])			
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testCreation() {
		//Preperation
		def col = Column.findByName('mycolumn')
		def user = User.findByUsername('testuser')
		def color = Color.findByColorCode('#FFFFFF')
		def prio = Priority.findByName('critital')		
		assertNotNull col
		assertNotNull user
		assertNotNull color
		assertNotNull prio
		
		def task = new Task(
			name: 'mytask', 
			durationHours: 0.5, 
			color: color, 
			priority: prio, 
			column: col, 
			creator: user,
			sortorder: 1
		)
		assertTrue task.validate()
		assertNotNull task.save()
		assertNotNull Task.findByName('mytask')		
    }
	
	void testCreationWithAssignee() {
		//Preperation
		def col = Column.findByName('mycolumn')
		def user = User.findByUsername('testuser')
		def color = Color.findByColorCode('#FFFFFF')
		def prio = Priority.findByName('critital')
		assertNotNull col
		assertNotNull user
		assertNotNull color
		assertNotNull prio
		
		def task = new Task(
			name: 'mytask',
			durationHours: 0.5,
			color: color,
			priority: prio,
			column: col,
			creator: user,
			assignee:user
		)
		assertTrue task.validate()
		assertNotNull task.save()
		assertNotNull Task.findByName('mytask')
		assertEquals 'testuser', Task.findByName('mytask').assignee.username
	}
	
	void testCreationDurationHoursInt() {
		//Preperation
		def col = Column.findByName('mycolumn')
		def user = User.findByUsername('testuser')
		def color = Color.findByColorCode('#FFFFFF')
		def prio = Priority.findByName('critital')
		assertNotNull col
		assertNotNull user
		assertNotNull color
		assertNotNull prio
		
		def task = new Task(
			name: 'mytask',
			durationHours: 1,
			color: color,
			priority: prio,
			column: col,
			creator: user
		)
		assertTrue task.validate()
		assertNotNull task.save()
		assertNotNull Task.findByName('mytask')
	}
	
	void testFailBlankName() {
		//Preperation
		def col = Column.findByName('mycolumn')
		def user = User.findByUsername('testuser')
		def color = Color.findByColorCode('#FFFFFF')
		def prio = Priority.findByName('critital')		
		assertNotNull col
		assertNotNull user
		assertNotNull color
		assertNotNull prio
		
		def task = new Task(			
			durationHours: 0.5,
			color: color,
			priority: prio,
			column: col,
			creator: user
		)
		
		assertFalse task.validate()
		assertEquals 1, task.errors.allErrors.size()
		assertNull task.save()
		assertNull Task.findByName('mytask')
	}
	
	void testFailTooLongName() {
		//Preperation
		def col = Column.findByName('mycolumn')
		def user = User.findByUsername('testuser')
		def color = Color.findByColorCode('#FFFFFF')
		def prio = Priority.findByName('critital')
		assertNotNull col
		assertNotNull user
		assertNotNull color
		assertNotNull prio
		
		def name = 'This name is much longer than 100 characters. This name is much longer than 100 characters. This name is much longer than 100 characters.'
		
		def task = new Task(
			name: name,
			durationHours: 0.5,
			color: color,
			priority: prio,
			column: col,
			creator: user
		)
		
		assertFalse task.validate()
		assertEquals 1, task.errors.allErrors.size()
		assertNull task.save()		
	}
	
	void testFailDurationHoursTooLong() {
		//Preperation
		def col = Column.findByName('mycolumn')
		def user = User.findByUsername('testuser')
		def color = Color.findByColorCode('#FFFFFF')
		def prio = Priority.findByName('critital')
		assertNotNull col
		assertNotNull user
		assertNotNull color
		assertNotNull prio			
		
		def task = new Task(
			name: 'mytask',
			durationHours: 800.25,
			color: color,
			priority: prio, 
			column: col,
			creator: user
		)
		
		assertFalse task.validate()
		assertEquals 1, task.errors.allErrors.size()
		assertNull task.save()
	}
	
	void testFailWithoutColor() {
		//Preperation
		def col = Column.findByName('mycolumn')
		def user = User.findByUsername('testuser')		
		def prio = Priority.findByName('critital')
		assertNotNull col
		assertNotNull user		
		assertNotNull prio
		
		def task = new Task(
			name: 'mytask',
			durationHours: 200.5,			
			priority: prio,
			column: col,
			creator: user
		)
		
		assertFalse task.validate()
		println task.errors.allErrors
		assertEquals 1, task.errors.allErrors.size()
		assertNull task.save()
	}
	
	void testFailWithoutPrio() {
		//Preperation
		def col = Column.findByName('mycolumn')
		def user = User.findByUsername('testuser')
		def color = Color.findByColorCode('#FFFFFF')		
		assertNotNull col
		assertNotNull user
		assertNotNull color		
		
		def task = new Task(
			name: 'mytask',
			durationHours: 200.5,
			color: color,			
			column: col,
			creator:user
		)
		
		assertFalse task.validate()
		assertEquals 1, task.errors.allErrors.size()
		assertNull task.save()
	}
	
	void testFailWithoutColumn() {
		//Preperation		
		def user = User.findByUsername('testuser')
		def color = Color.findByColorCode('#FFFFFF')
		def prio = Priority.findByName('critital')		
		assertNotNull user
		assertNotNull color
		assertNotNull prio
		
		def task = new Task(
			name: 'mytask',
			durationHours: 200.25,
			color: color,
			priority: prio,			
			creator: user
		)
		
		assertFalse task.validate()
		assertEquals 1, task.errors.allErrors.size()
		assertNull task.save()
	}
	
	void testFailWithoutcreator() {
		//Preperation
		def col = Column.findByName('mycolumn')		
		def color = Color.findByColorCode('#FFFFFF')
		def prio = Priority.findByName('critital')
		assertNotNull col		
		assertNotNull color
		assertNotNull prio
		
		def task = new Task(
			name: 'mytask',
			durationHours: 200.25,
			color: color,
			priority: prio,
			column: col
		)
		
		assertFalse task.validate()
		assertEquals 1, task.errors.allErrors.size()
		assertNull task.save()
	}
}
