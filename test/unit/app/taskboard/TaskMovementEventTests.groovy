package app.taskboard

import grails.test.*

class TaskMovementEventTests extends TaskBoardUnitTest {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {   
        super.tearDown()
    }

    void testEventCreation() {
		def col1 = Column.findByName('todo')
		def col2 = Column.findByName('wip')
		
		def task = Task.findByName('todotask')
		
		def event = new TaskMovementEvent(task: task, fromColumn: col1, tooColumn: col2, user: User.findByUsername('testuser'))
		def result = event.validate()
		event.errors.allErrors.each {println it}
		assertTrue result
		assertNotNull event.save()
		assertNotNull event.dateCreated
		
    }
	
	void failOnUpdate() {
		def col1 = Column.findByName('todo')
		def col2 = Column.findByName('wip')
		def task = Task.findByName('mytask')
		
		def event = new TaskMovementEvent(task: task, fromColumn: col1, tooColumn: col2, user: User.findByUsername('testuser'))
		assertTrue event.validate()
		assertNotNull event.save()
		
		try{
			event.dateCreated = new Date()
			event.save()
		}
		catch(Exception e) {
			assertEquals 'EventUpdateException', e.class.name
		}
		
	}
}
