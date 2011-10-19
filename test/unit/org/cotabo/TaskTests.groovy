package org.cotabo

import grails.test.*
import org.codehaus.groovy.grails.plugins.codecs.HTMLCodec

class TaskTests extends TaskBoardUnitTest {		
    protected void setUp() {
        super.setUp()
		loadCodec(HTMLCodec)
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
			priority: 'Critical'
		).addToColors(new TaskColor(color:'faf77a', name:'not needed'))
		
		assertTrue task.validate()		
		assertNotNull task.save()
		assertNotNull task.dateCreated
		assertNotNull task.lastUpdated
		assertNotNull Task.findByName('mytask')		
    }
	
	void testCreationWithAssignee() {
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
			assignee:user,
			priority: 'Critical'
		).addToColors(new TaskColor(color:'faf77a', name:'not needed'))
		
		assertTrue task.validate()
		assertNotNull task.save()
		assertNotNull Task.findByName('mytask')
		assertEquals 'testuser', Task.findByName('mytask').assignee.username
	}
	
	void testCreationDurationHoursInt() {
		//Preparation
		def col = Column.findByName('todo')
		def user = User.findByUsername('testuser')				
		assertNotNull col
		assertNotNull user				
		
		def task = new Task(
			name: 'mytask',
			durationHours: 1,			
			column: col,
			creator: user,
			priority: 'Critical'
		).addToColors(new TaskColor(color:'faf77a', name:'not needed'))
		
		assertTrue task.validate()
		assertNotNull task.save()
		assertNotNull Task.findByName('mytask')
	}
	
	void testFailBlankName() {
		//Preparation
		def col = Column.findByName('todo')
		def user = User.findByUsername('testuser')				
		assertNotNull col
		assertNotNull user				
		
		def task = new Task(			
			durationHours: 0.5,						
			column: col,
			creator: user,
			priority: 'Critical'
		).addToColors(new TaskColor(color:'faf77a', name:'not needed'))
		
		assertFalse task.validate()
		assertEquals 1, task.errors.allErrors.size()
		assertNull task.save()
		assertNull Task.findByName('mytask')
	}
	
	void testFailTooLongName() {
		//Preparation
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
			priority: 'Critical'
		).addToColors(new TaskColor(color:'faf77a', name:'not needed'))
		
		assertFalse task.validate()
		assertEquals 1, task.errors.allErrors.size()
		assertNull task.save()		
	}
	
	void testFailDurationHoursTooLong() {
		//Preparation
		def col = Column.findByName('todo')
		def user = User.findByUsername('testuser')				
		assertNotNull col
		assertNotNull user				
		
		def task = new Task(
			name: 'mytask',
			durationHours: 800.25,			
			column: col,
			creator: user,
			priority: 'Critical'
		).addToColors(new TaskColor(color:'faf77a', name:'not needed'))
		
		assertFalse task.validate()
		assertEquals 1, task.errors.allErrors.size()
		assertNull task.save()
	}
	
	
	void testFailWithoutColumn() {
		//Preparation		
		def user = User.findByUsername('testuser')					
		assertNotNull user				
		
		def task = new Task(
			name: 'mytask',
			durationHours: 200.25,						
			creator: user,
			priority: 'Critical'
		).addToColors(new TaskColor(color:'faf77a', name:'not needed'))
		
		assertFalse task.validate()
		assertEquals 1, task.errors.allErrors.size()
		assertNull task.save()
	}
	
	void testFailWithoutcreator() {
		//Preparation
		def col = Column.findByName('todo')						
		assertNotNull col						
		
		def task = new Task(
			name: 'mytask',
			durationHours: 200.25,						
			column: col,
			priority: 'Critical'
		).addToColors(new TaskColor(color:'faf77a', name:'not needed'))
		
		assertFalse task.validate()
		assertEquals 1, task.errors.allErrors.size()
		assertNull task.save()
	}
	
	void testWithoutColor() {
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
			priority: 'Critical'
		)
		assertTrue task.validate()
		assertNotNull task.save()
		assertNotNull Task.findByName('mytask')
	}
	
	
	
	void testFailWithoutPriority() {
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
		).addToColors(new TaskColor(color:'faf77a', name:'not needed'))
		
		assertFalse task.validate()
		assertNull task.save()
		assertNull Task.findByName('mytask')
	}
	
	void testSuccessNotConfiguredColor() {
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
			priority: 'Critical'
		)
		
		assertTrue task.validate()
		assertNotNull task.save()
		assertNotNull Task.findByName('mytask')
	}
	
	void testFailNotConfiguredPriority() {
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
			priority: 'HyperCritical'
		).addToColors(new TaskColor(color:'faf77a', name:'not needed'))
		
		assertFalse task.validate()
		assertNull task.save()
		assertNull Task.findByName('mytask')
	}
	
	void testToMessage() {
		def expected = "[id:null, creator:testuser, assignee:testuser, sortorder:0, dateCreated:Thu Jan 01 01:00:00 CET 1970, workflowStartDate:null, workflowEndDate:null, name:mytask, description:null, details:null, durationHours:0.5, priority:Critical, colors:[[color:faf77a, name:not needed]], blocked:false]"
		
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
			assignee:user,
			priority: 'Critical',
			dateCreated: new Date(0l)
		).addToColors(new TaskColor(color:'faf77a', name:'not needed'))
		
		assertTrue task.validate()
		//assertNotNull task.save()
		
		def message = task.toMessage()
		assertEquals expected.toString(), message.toString()
	}
}
