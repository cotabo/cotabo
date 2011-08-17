package org.cotabo

import grails.test.*

class TaskIntegrationTests extends GrailsUnitTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

	
	void testCreationAddToColumn() {
		//Preperation
		def col = Column.findByName('ToDo')
		def othercol = Column.findByName('In Progress')
		def user = User.findByUsername('admin')				
		
		assertNotNull col
		assertNotNull othercol
		assertNotNull user				
		
		def newCol =othercol.addToTasks(
			name: 'mytask',
			durationHours: 0.5,
			creator: user,
			column: col,
			sortorder: 100,
			priority: 'Critical',
			color: '#faf77a'
		)
		assertTrue newCol.validate()
		assertNotNull newCol.save(flush:true)
		
		def tmpTask = Task.findByName('mytask')
		assertNotNull tmpTask
		assertEquals 'In Progress', tmpTask.column.name
	}
	
	void testSetBlocked() {
		//Getting a test task created in super class
		def task = Task.findByName('Task 3')
		
		task.setBlocked(true)
		task.save(flush:true);
		assertTrue task.blocked
		
		task.setBlocked(false)
		task.save(flush:true)
		assertFalse task.blocked			
	}
	
	void testSettingBlockedMultipleTimes() {
		def task = Task.findByName('Task 4')
		task.blocked = true
		task.blocked = true
		task.save()
		assertEquals 1, task.blocks.size()
		
	}
}
