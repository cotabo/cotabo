package app.taskboard

import grails.test.*

class TaskIntegrationTests extends GrailsUnitTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

	
	void testCreationAddToColumn() {
		//Preperation
		def col = Column.findByName('ToDo')
		def user = User.findByUsername('admin')
		def color = Color.findByColorCode('#FFFFFF')
		def prio = Priority.findByName('critical')
		
		assertNotNull col
		assertNotNull user
		assertNotNull color
		assertNotNull prio
		
		def newCol =col.addToTasks(
			name: 'mytask',
			durationHours: 0.5,
			color: color,
			priority: prio,
			creator: user
		)
		assertTrue newCol.validate()
		assertNotNull newCol.save()
		
		def tmpTask = Task.findByName('mytask')
		assertNotNull tmpTask
		assertEquals 'ToDo', tmpTask.column.name
	}
}
