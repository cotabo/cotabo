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
		def expectedResult = ''
		def controller = new TaskController()
		def todo = Column.get(2)
		def done = Column.get(3)
		def moveTask = todo.tasks.first()
		controller.params.fromColumn = todo.id
		controller.params.toColumn = done.id
		//The 'Setup WebLogic' task
		controller.params.taskid = moveTask.id
		controller.params.toIndex = '0'
		
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
		def expectedResult = '<div class="alert-message block-message error"><p>One of the following objects does not exist: column: 2, column: 3, task: null</p></div>'
		def controller = new TaskController()
		controller.params.fromColumn = 2
		controller.params.toColumn = 3
		def result
		SpringSecurityUtils.doWithAuth('user') {
			 result = controller.move()
		}
		assertNotNull controller.response.contentAsString
		assertEquals expectedResult, controller.response.contentAsString
	}
	
	void testInvalidRespondeForMissingColumn() {
				def expectedResult = '<div class="alert-message block-message error"><p>One of the following objects does not exist: column: 2, column: null, task: 2</p></div>'
		def controller = new TaskController()
		controller.params.fromColumn = 2
		controller.params.taskid = 2
		def result 
		SpringSecurityUtils.doWithAuth('user') {
			result = controller.move()
		}
		assertNotNull controller.response.contentAsString
		assertEquals expectedResult, controller.response.contentAsString
		
	}
}
