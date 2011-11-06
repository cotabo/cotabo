package org.cotabo

import grails.test.*
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils

class TaskControllerTests extends GrailsUnitTestCase {
    protected void setUp() {
        super.setUp()				
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testValidResponse() {
		def expectedResult = '{"returncode":0,"message":""}'
		def controller = new TaskController()
		def todo = Column.get(2)
		def done = Column.get(3)
		def moveTask = todo.tasks.first()
		controller.params.fromColumn = todo.id
		controller.params.toColumn = done.id
		//The 'Setup WebLogic' task
		controller.params.taskid = moveTask.id
		
		def result
		//We need to be authenticated for that
		SpringSecurityUtils.doWithAuth('user') {
			result = controller.move()
		}			
		assertNotNull controller.response.contentAsString
		assertEquals expectedResult, controller.response.contentAsString 				
		assertEquals Column.get(3), moveTask.column 
    }
	
	void testInvalidRespondeForMissingTask() {
		def expectedResult = '{"returncode":1,"message":"Either the column or the Task that you have specified does not exist."}'
		def controller = new TaskController()
		controller.params.id = 2
		controller.params.toColumn = 3
		def result
		SpringSecurityUtils.doWithAuth('user') {
			 result = controller.move()
		}
		assertNotNull controller.response.contentAsString
		assertEquals expectedResult, controller.response.contentAsString
	}
	
	void testInvalidRespondeForMissingColumn() {
		def expectedResult = '{"returncode":1,"message":"Either the column or the Task that you have specified does not exist."}'
		def controller = new TaskController()
		controller.params.id = 100
		controller.params.taskid = 2
		def result 
		SpringSecurityUtils.doWithAuth('user') {
			result = controller.move()
		}
		assertNotNull controller.response.contentAsString
		assertEquals expectedResult, controller.response.contentAsString
		
	}
}
