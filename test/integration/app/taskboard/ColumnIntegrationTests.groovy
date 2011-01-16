package app.taskboard

import grails.test.*

class ColumnIntegrationTests extends GrailsUnitTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }
	
	void testblah() {
		Column.list()[0].tasks.each {
			println it.sortorder
		}
		
	}


}
