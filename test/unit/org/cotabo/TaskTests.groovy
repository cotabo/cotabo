package org.cotabo

import grails.test.*

class TaskTests extends TaskBoardUnitTest {		
    protected void setUp() {
        super.setUp()		
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testCreation() {
		//Preparation
		def col = Column.findByName('todo')
		def user = User.findByUsername('testuser')				
		assertNotNull col
		assertNotNull user				
		
		def task = new Task(
			name: 'mytask', 
			durationHours: 0.5, 						
			column: col, 
			creator: user,
			sortorder: 1,
			color: new TaskColor(color:'faf77a', name:'not needed'),
			priority: 'Critical'
		)
		assertTrue task.validate()		
		assertNotNull task.save()
		assertNotNull task.dateCreated
		assertNotNull task.lastUpdated
		assertNotNull Task.findByName('mytask')		
    }
	
	void testCreationWithAssignee() {
		//Preperation
		def col = Column.findByName('todo')
		def user = User.findByUsername('testuser')				
		assertNotNull col
		assertNotNull user				
		
		def task = new Task(
			name: 'mytask',
			durationHours: 0.5,			
			column: col,
			creator: user,
			assignee:user,
			color: new TaskColor(color:'faf77a', name:'not needed'),
			priority: 'Critical'
		)
		assertTrue task.validate()
		assertNotNull task.save()
		assertNotNull Task.findByName('mytask')
		assertEquals 'testuser', Task.findByName('mytask').assignee.username
	}
	
	void testCreationDurationHoursInt() {
		//Preperation
		def col = Column.findByName('todo')
		def user = User.findByUsername('testuser')				
		assertNotNull col
		assertNotNull user				
		
		def task = new Task(
			name: 'mytask',
			durationHours: 1,			
			column: col,
			creator: user,
			color: new TaskColor(color:'faf77a', name:'not needed'),
			priority: 'Critical'
		)
		assertTrue task.validate()
		assertNotNull task.save()
		assertNotNull Task.findByName('mytask')
	}
	
	void testFailBlankName() {
		//Preperation
		def col = Column.findByName('todo')
		def user = User.findByUsername('testuser')				
		assertNotNull col
		assertNotNull user				
		
		def task = new Task(			
			durationHours: 0.5,						
			column: col,
			creator: user,
			color: new TaskColor(color:'faf77a', name:'not needed'),
			priority: 'Critical'
		)
		
		assertFalse task.validate()
		assertEquals 1, task.errors.allErrors.size()
		assertNull task.save()
		assertNull Task.findByName('mytask')
	}
	
	void testFailTooLongName() {
		//Preperation
		def col = Column.findByName('todo')
		def user = User.findByUsername('testuser')				
		assertNotNull col
		assertNotNull user				
		
		def name = 'This name is much longer than 100 characters. This name is much longer than 100 characters. This name is much longer than 100 characters.'
		
		def task = new Task(
			name: name,
			durationHours: 0.5,						
			column: col,
			creator: user,
			color: new TaskColor(color:'faf77a', name:'not needed'),
			priority: 'Critical'
		)
		
		assertFalse task.validate()
		assertEquals 1, task.errors.allErrors.size()
		assertNull task.save()		
	}
	
	void testFailDurationHoursTooLong() {
		//Preperation
		def col = Column.findByName('todo')
		def user = User.findByUsername('testuser')				
		assertNotNull col
		assertNotNull user				
		
		def task = new Task(
			name: 'mytask',
			durationHours: 800.25,			
			column: col,
			creator: user,
			color: new TaskColor(color:'faf77a', name:'not needed'),
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
			color: new TaskColor(color:'faf77a', name:'not needed'),
			priority: 'Critical'
		)
		
		assertFalse task.validate()
		assertEquals 1, task.errors.allErrors.size()
		assertNull task.save()
	}
	
	void testFailWithoutcreator() {
		//Preperation
		def col = Column.findByName('todo')						
		assertNotNull col						
		
		def task = new Task(
			name: 'mytask',
			durationHours: 200.25,						
			column: col,
			color: new TaskColor(color:'faf77a', name:'not needed'),
			priority: 'Critical'
		)
		
		assertFalse task.validate()
		assertEquals 1, task.errors.allErrors.size()
		assertNull task.save()
	}
	
	void testWithoutColor() {
		//Preperation
		def col = Column.findByName('todo')
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
		assertTrue task.validate()
		assertNotNull task.save()
		assertNotNull Task.findByName('mytask')
	}
	
	
	
	void testFailWithoutPriority() {
		//Preperation
		def col = Column.findByName('todo')
		def user = User.findByUsername('testuser')
		assertNotNull col
		assertNotNull user
		
		def task = new Task(
			name: 'mytask',
			durationHours: 0.5,
			column: col,
			creator: user,
			sortorder: 1,
			color: new TaskColor(color:'faf77a', name:'not needed')			
		)
		assertFalse task.validate()
		assertNull task.save()
		assertNull Task.findByName('mytask')
	}
	
	void testFailNotConfiguredColor() {
		//Preperation
		def col = Column.findByName('todo')
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
		assertTrue task.validate()
		assertNotNull task.save()
		assertNotNull Task.findByName('mytask')
	}
	
	void testFailNotConfiguredPriority() {
		//Preperation
		def col = Column.findByName('todo')
		def user = User.findByUsername('testuser')
		assertNotNull col
		assertNotNull user
		
		def task = new Task(
			name: 'mytask',
			durationHours: 0.5,
			column: col,
			creator: user,
			sortorder: 1,
			color: new TaskColor(color:'faf77a', name:'not needed'),
			priority: 'HyperCritical'
		)
		assertFalse task.validate()
		assertNull task.save()
		assertNull Task.findByName('mytask')
	}
}
