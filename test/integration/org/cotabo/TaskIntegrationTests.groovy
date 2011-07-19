package org.cotabo

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
		
		assertNotNull col
		assertNotNull user				
		
		def newCol =col.addToTasks(
			name: 'mytask',
			durationHours: 0.5,
			creator: user,
			sortorder: 100,
			priority: 'Critical',
			color: '#faf77a'
		)
		assertTrue newCol.validate()
		assertNotNull newCol.save()
		
		def tmpTask = Task.findByName('mytask')
		assertNotNull tmpTask
		assertEquals 'ToDo', tmpTask.column.name
	}
}
