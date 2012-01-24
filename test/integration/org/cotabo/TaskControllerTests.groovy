package org.cotabo

import grails.test.*
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils

class TaskControllerTests extends GroovyTestCase {
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
		def moveTask = Task.get(3)
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
	
	void testMoveTask(){
		def controller = new TaskController()
		def user = User.findByUsername('admin')
		def todo = Column.get(1)
		def inprogress = Column.get(2)
		def initial = inprogress.tasks.size() as int
		def expected = initial + 1
		controller.params.fromColumn=todo.id
		controller.params.toColumn=inprogress.id
		controller.params.taskid=3
		controller.params.toIndex=1
		
		def result
		SpringSecurityUtils.doWithAuth('user') {
			result = controller.move()
		}
		
		assertEquals expected, inprogress.tasks.size()
		
	}
	
	void testSaveTask(){
		def controller = new TaskController()
		def user = User.findByUsername('admin')
		def todo = Column.get(1)
		def initial = todo.tasks.size() as int
		def expected = initial + 1
		controller.params.name="New Task"
		controller.params.durationHours=1D
		controller.params.column=todo.id
		controller.params.creator="admin"
		controller.params.assignee="${user.id}"
		controller.params.priority="Normal"
		controller.params.board=todo.board.id
		
		def result
		SpringSecurityUtils.doWithAuth('user') {
			result = controller.save()
		}
		
		assertEquals expected, todo.tasks.size()
		assertEquals 1, todo.tasks.findAll {it && it.name=="New Task"}.size()
	}
}
