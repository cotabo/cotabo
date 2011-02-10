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
		controller.params.fromColumnId = 1
		controller.params.toColumnid = 2
		controller.params.taskid = 2
		def result = controller.updatetasks()			
		assertNotNull controller.response.contentAsString
		assertEquals expectedResult, controller.response.contentAsString 
		def expectedTask = Task.findByName('Bootstrap Webserver')
		def expectedColumn = Column.findByName('In Progress')
		assertEquals expectedTask.column, expectedColumn
		def task = Task.get(1)
		assertEquals task.column, expectedColumn
		assertNotNull Column.get(2).tasks.find {it.id == 1}
		assertEquals Task.get(1).column.id , 2		
		assertNull Task.list().find { it == null }
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
		controller.params.taskid = 1
		def result = controller.updatetasks()
		assertNotNull controller.response.contentAsString
		assertEquals expectedResult, controller.response.contentAsString
		
	}
}
