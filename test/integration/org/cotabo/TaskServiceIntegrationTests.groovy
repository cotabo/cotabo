package org.cotabo

import grails.test.*
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils

class TaskServiceIntegrationTests extends GrailsUnitTestCase {
	def taskService
	def springSecurityUtils
	
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }
	
	void testMoveTask() {			
		def expectedColumnStatusEntrySize = ColumnStatusEntry.list().size() + 3
		def taskToMove =  Column.get(1).tasks.first()
		def initialTaskCount = Column.get(2).tasks.size()
		println taskToMove
		println Column.get(2).tasks
		//We need to be authenticated for that				
		SpringSecurityUtils.doWithAuth('user') {
			taskService.moveTask(taskToMove.column, Column.get(2), taskToMove)			
		}		
		println Column.get(2).tasks		
		assertEquals initialTaskCount + 1, Column.get(2).tasks.size()		
		
		//Testing the generated events				
		assertEquals expectedColumnStatusEntrySize, ColumnStatusEntry.list().size()		

		//Test whether the assignment works
		assertEquals 'user', Task.get(3).assignee.username

		//We need to be authenticated for that
		SpringSecurityUtils.doWithAuth('user') {
			//Move another task an see whether the tasks on the event objects stay the same
			taskService.moveTask(Column.get(1), Column.get(2), Task.get(4))
		}		
		assertEquals expectedColumnStatusEntrySize + 3, ColumnStatusEntry.list().size()				
		
	}
	
	void testMoveTaskWithPosition() {
		def taskToMove =  Column.get(1).tasks.first()
		def fromColumn = Column.get(1)
		def toColumn = Column.get(2)
		def position = 1
		def expected = Column.get(2).tasks.collect{it}
		expected.add(1, taskToMove)
		
		SpringSecurityUtils.doWithAuth('user') {
			taskService.moveTask(fromColumn, toColumn, taskToMove, position)
		}
		
		assertEquals expected, Column.get(2).tasks		
	}
	
	void testSaveTask() {
		def col = Column.findByName('ToDo')
		def user = User.findByUsername('admin')
		
		def task = new Task(
			name: 'mytask',
			durationHours: 0.5,
			creator: user,			
			priority: 'Critical',
			color: '#fafaa8',
			column: col
		)

		//We need to be authenticated for that
		SpringSecurityUtils.doWithAuth('user') {
			task = taskService.saveTask(task)
		}		
		assertEquals 0, task.errors.errorCount 
		
		//Check if our tasks appears in the last ColumnStatusEntry - was 5 - should be 6
		assertEquals 6, ColumnStatusEntry.findAllByColumn(Column.findByName('ToDo')).last().tasks				
	}
}