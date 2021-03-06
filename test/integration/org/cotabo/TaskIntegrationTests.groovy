package org.cotabo

import grails.test.*

class TaskIntegrationTests extends GrailsUnitTestCase {
	def rerenderService
	def grailsApplication
	
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

	
	void testCreationAddToColumn() {
		//Preperation
		def col = Column.findByName('Done!')		
		def user = User.findByUsername('admin')				
		
		assertNotNull col		
		assertNotNull user
		
		def newtask = [
			name: 'mytask',
			durationHours: 0.5,
			creator: user,	
			column: col,				
			priority: 'Critical',
			color: grailsApplication.config.taskboard.colors[0]
		] as Task		
		
		col.addToTasks(newtask)
		
		assertTrue col.validate()
		assertNotNull col.save(flush:true)
		
		def tmpTask = Task.findByName('mytask')
		assertNotNull tmpTask
		assertEquals 'Done!', tmpTask.column.name
	}
	
	void testInitialBlocked() {
		def task = Task.findByName('Bootstrap Webserver')
		
		assertTrue task.blocked
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
	
	void testRerender() {		
		def task = Task.findByName('Setup WebLogic')
		def expected = '/task/showDom/' + task.id
		
		assertEquals expected, rerenderService.render(task)
		
	}
}
