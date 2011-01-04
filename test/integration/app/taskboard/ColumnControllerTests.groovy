package app.taskboard

import grails.test.*

class ColumnControllerTests extends GrailsUnitTestCase {
    protected void setUp() {
        super.setUp()		
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testValidResponse() {
		def expectedResult = '{"returncode":0,"message":""}'
		def controller = new ColumnController()
		controller.params.id = 2
		controller.params.taskid = 1
		def result = controller.updatetasks()			
		assertNotNull controller.response.contentAsString
		assertEquals expectedResult, controller.response.contentAsString 
		def expectedTask = Task.findByName('Bootstrap Webserver')
		def expectedColumn = Column.findByName('In Progress')
		assertEquals expectedTask.column, expectedColumn
		
    }
}
