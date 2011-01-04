package app.taskboard

import grails.test.*

class ColorTests extends GrailsUnitTestCase {
    protected void setUp() {
		super.setUp()
		mockDomain(Color)
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testColorCreation() {
		def color = new Color(colorCode:'#FFFFFF')
		assertTrue color.validate()
		assertNotNull color.save()
    }
	
	void testFailTooLongColorCode() {
		def color = new Color(colorCode:'#1234567')
		assertFalse color.validate()
		assertNull color.save()
	}
	
	void testFailTooSmallColorCode() {
		def color = new Color(colorCode:'#12345')
		assertFalse color.validate()
		assertNull color.save()
	}
	
	void testFailColorCodeWithoutHash() {
		def color = new Color(colorCode:'1234567')
		assertFalse color.validate()
		assertNull color.save()
	}
}
