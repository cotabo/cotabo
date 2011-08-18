package org.cotabo

import grails.test.*

class BlockTests extends GroovyTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testCreateBlock() {
			
			def task = Task.findByName('Task 2')
			assertNotNull task
			
			task.setBlocked(true)
			task.setBlocked(false)
			
			def block = task.blocks[-1]
			assertNotNull block
			assertNotNull block.dateCreated
			assertNotNull block.dateClosed
    }
}
