package org.cotabo

import grails.test.*

class TaskColorTests extends GrailsUnitTestCase {
    protected void setUp() {
        super.setUp()
		mockDomain(TaskColor)
		mockDomain(Task)
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testValidColorValidName() {
		def color1 = new TaskColor(name:"none", color:"123456")
		assertNotNull color1.validate()
		assertNotNull color1.save()
    }
	
	void testValidColorWithHashValidName() {
		def color1 = new TaskColor(name:"none", color:"#123456")
		assertNotNull color1.validate()
		assertNotNull color1.save()
	}
	
	void testInvalidColorValidName() {
		def color1 = new TaskColor(name:"none", color:"123zz6")
		assertFalse color1.validate()
		assertNull color1.save()
	}
	
	void testInvalidColorInvalidName() {
		def color1 = new TaskColor(color:"123zz6")
		assertFalse color1.validate()
		assertNull color1.save()
	}
	
	void testValidColorInvalidName() {
		def color1 = new TaskColor(color:"123456")
		assertFalse color1.validate()
		assertNull color1.save()
	}
}
