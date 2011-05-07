package app.taskboard

import grails.test.*
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils

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
		controller.params.fromColumn = 1
		controller.params.toColumn = 2
		controller.params.taskid = 2
		def result
		//We need to be authenticated for that
		SpringSecurityUtils.doWithAuth('user') {
			result = controller.updatetasks()
		}			
		assertNotNull controller.response.contentAsString
		assertEquals expectedResult, controller.response.contentAsString 
		def expectedTask = Task.findByName('Setup WebLogic')		
		def expectedColumn = Column.findByName('In Progress')
		assertEquals expectedColumn, expectedTask.column 
    }
	
	void testInvalidRespondeForMissingTask() {
		def expectedResult = '{"returncode":1,"message":"Either the column or the Task that you have specified does not exist."}'
		def controller = new ColumnController()
		controller.params.id = 2
		controller.params.taskid = 100
		def result = controller.updatetasks()
		assertNotNull controller.response.contentAsString
		assertEquals expectedResult, controller.response.contentAsString
	}
	
	void testInvalidRespondeForMissingColumn() {
		def expectedResult = '{"returncode":1,"message":"Either the column or the Task that you have specified does not exist."}'
		def controller = new ColumnController()
		controller.params.id = 100
		controller.params.taskid = 2
		def result = controller.updatetasks()
		assertNotNull controller.response.contentAsString
		assertEquals expectedResult, controller.response.contentAsString
		
	}
}
