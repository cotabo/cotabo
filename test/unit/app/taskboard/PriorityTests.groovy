package app.taskboard

import grails.test.*

class PriorityTests extends GrailsUnitTestCase {
    protected void setUp() {
        super.setUp()
		mockDomain(Priority)
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testCreation() {
		def prio = new Priority(name:'critical')
		assertTrue prio.validate()
		assertNotNull prio.save()		
    }
	
	void testFailNameNull() {
		def prio = new Priority()
		assertFalse prio.validate()
		assertEquals 1, prio.errors.allErrors.size()
		assertNull prio.save()
	}
}
