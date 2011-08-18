package org.cotabo

import grails.test.*

class BlockTests extends TaskBoardUnitTest {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }
	
    void testSuccessOnCreationWithoutTask() {
			mockDomain(Block)
			def block = new Block()
			assertTrue block.validate()
			assertNotNull block.save()					
		}
}
