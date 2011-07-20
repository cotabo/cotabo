package org.cotabo

import grails.test.*

class BlockTests extends TaskBoardUnitTest {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testCreateBlock() {
		mockDomain(Block)
		def task = Task.findByName('testtask2')
		def block = new Block(task:task)
		assertTrue block.validate()
		assertNotNull block.save()		
		assertNotNull Block.findByTask(task)
		assertNotNull block.dateCreated
		assertNull block.dateClosed			
    }
	
	void testFailOnCreationWithoutTask() {
		mockDomain(Block)
		def block = new Block()
		assertFalse block.validate()
		assertNull block.save()					
	}
}
