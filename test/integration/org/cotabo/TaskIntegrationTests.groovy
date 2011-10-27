package org.cotabo

import grails.test.*
import org.codehaus.groovy.grails.commons.ConfigurationHolder as grailsConfig

class TaskIntegrationTests extends GrailsUnitTestCase {
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
			sortorder: 100,
			priority: 'Critical',
			color: grailsConfig.config.taskboard.colors[0]
		] as Task
		
		newtask.save(flush:true)
		
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
}
