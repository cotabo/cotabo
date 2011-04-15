package app.taskboard

import grails.test.*

class TaskTests extends GrailsUnitTestCase {
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
		def board = new Board(name:'myboard')
		def user = User.findByUsername('testuser')		
		board.columns = [column]
		board.users = [user]
		column.board = board
		
		mockDomain(Task)
		mockDomain(Column, [column])
		mockDomain(Board, [board])
		
		
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testCreation() {
		//Preperation
		def col = Column.findByName('mycolumn')
		def user = User.findByUsername('testuser')				
		assertNotNull col
		assertNotNull user				
		
		def task = new Task(
			name: 'mytask', 
			durationHours: 0.5, 						
			column: col, 
			creator: user,
			sortorder: 1,
			color: '#faf77a',
			priority: 'Critical'
		)
		assertTrue task.validate()
		assertNotNull task.save()
		assertNotNull Task.findByName('mytask')		
    }
	
	void testCreationWithAssignee() {
		//Preperation
		def col = Column.findByName('mycolumn')
		def user = User.findByUsername('testuser')				
		assertNotNull col
		assertNotNull user				
		
		def task = new Task(
			name: 'mytask',
			durationHours: 0.5,			
			column: col,
			creator: user,
			assignee:user,
			color: '#faf77a',
			priority: 'Critical'
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
		assertNotNull col
		assertNotNull user				
		
		def task = new Task(
			name: 'mytask',
			durationHours: 1,			
			column: col,
			creator: user,
			color: '#faf77a',
			priority: 'Critical'
		)
		assertTrue task.validate()
		assertNotNull task.save()
		assertNotNull Task.findByName('mytask')
	}
	
	void testFailBlankName() {
		//Preperation
		def col = Column.findByName('mycolumn')
		def user = User.findByUsername('testuser')				
		assertNotNull col
		assertNotNull user				
		
		def task = new Task(			
			durationHours: 0.5,						
			column: col,
			creator: user,
			color: '#faf77a',
			priority: 'Critical'
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
		assertNotNull col
		assertNotNull user				
		
		def name = 'This name is much longer than 100 characters. This name is much longer than 100 characters. This name is much longer than 100 characters.'
		
		def task = new Task(
			name: name,
			durationHours: 0.5,						
			column: col,
			creator: user,
			color: '#faf77a',
			priority: 'Critical'
		)
		
		assertFalse task.validate()
		assertEquals 1, task.errors.allErrors.size()
		assertNull task.save()		
	}
	
	void testFailDurationHoursTooLong() {
		//Preperation
		def col = Column.findByName('mycolumn')
		def user = User.findByUsername('testuser')				
		assertNotNull col
		assertNotNull user				
		
		def task = new Task(
			name: 'mytask',
			durationHours: 800.25,			
			column: col,
			creator: user,
			color: '#faf77a',
			priority: 'Critical'
		)
		
		assertFalse task.validate()
		assertEquals 1, task.errors.allErrors.size()
		assertNull task.save()
	}
	
	
	void testFailWithoutColumn() {
		//Preperation		
		def user = User.findByUsername('testuser')					
		assertNotNull user				
		
		def task = new Task(
			name: 'mytask',
			durationHours: 200.25,						
			creator: user,
			color: '#faf77a',
			priority: 'Critical'
		)
		
		assertFalse task.validate()
		assertEquals 1, task.errors.allErrors.size()
		assertNull task.save()
	}
	
	void testFailWithoutcreator() {
		//Preperation
		def col = Column.findByName('mycolumn')						
		assertNotNull col						
		
		def task = new Task(
			name: 'mytask',
			durationHours: 200.25,						
			column: col,
			color: '#faf77a',
			priority: 'Critical'
		)
		
		assertFalse task.validate()
		assertEquals 1, task.errors.allErrors.size()
		assertNull task.save()
	}
	
	void testFailWithoutColor() {
		//Preperation
		def col = Column.findByName('mycolumn')
		def user = User.findByUsername('testuser')
		assertNotNull col
		assertNotNull user
		
		def task = new Task(
			name: 'mytask',
			durationHours: 0.5,
			column: col,
			creator: user,
			sortorder: 1,			
			priority: 'Critical'
		)
		assertFalse task.validate()
		assertNull task.save()
		assertNull Task.findByName('mytask')
	}
	
	
	
	void testFailWithoutPriority() {
		//Preperation
		def col = Column.findByName('mycolumn')
		def user = User.findByUsername('testuser')
		assertNotNull col
		assertNotNull user
		
		def task = new Task(
			name: 'mytask',
			durationHours: 0.5,
			column: col,
			creator: user,
			sortorder: 1,
			color: '#faf77a'			
		)
		assertFalse task.validate()
		assertNull task.save()
		assertNull Task.findByName('mytask')
	}
	
	void testFailNotConfiguredColor() {
		//Preperation
		def col = Column.findByName('mycolumn')
		def user = User.findByUsername('testuser')
		assertNotNull col
		assertNotNull user
		
		def task = new Task(
			name: 'mytask',
			durationHours: 0.5,
			column: col,
			creator: user,
			sortorder: 1,
			color: '#ffffff',
			priority: 'Critical'
		)
		assertFalse task.validate()
		assertNull task.save()
		assertNull Task.findByName('mytask')
	}
	
	void testFailNotConfiguredPriority() {
		//Preperation
		def col = Column.findByName('mycolumn')
		def user = User.findByUsername('testuser')
		assertNotNull col
		assertNotNull user
		
		def task = new Task(
			name: 'mytask',
			durationHours: 0.5,
			column: col,
			creator: user,
			sortorder: 1,
			color: '#faf77a',
			priority: 'HyperCritical'
		)
		assertFalse task.validate()
		assertNull task.save()
		assertNull Task.findByName('mytask')
	}	
}
