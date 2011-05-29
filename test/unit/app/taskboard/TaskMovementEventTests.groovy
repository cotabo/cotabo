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
		
	
	void testWorkflowTimestampSetting() {
		def col1 = Column.findByName('todo')
		def col2 = Column.findByName('wip')
		def col3 = Column.findByName('done')
		
		def task = new Task(
			name: 'workflowtesttask',
			durationHours: 0.5,
			column: col1,
			creator: User.findByUsername('testuser'),
			sortorder: 1,
			color: '#faf77a',
			priority: 'Critical'
		)
		taskService.saveTask task
		//We emulate a movement from todo > wip (only the events for that)
		def event1 = new TaskMovementEvent(task: task, fromColumn: col1, tooColumn: col2, user: User.findByUsername('testuser'))
		event1.save()
		//We emulate a movement from wip > done (only the events for that)
		def event2 = new TaskMovementEvent(task: task, fromColumn: col2, tooColumn: col3, user: User.findByUsername('testuser'))
		event2.save()
		
		//Now we should have the workflowStartDate & workflowEndDate setted properly on the task
		assertNotNull Task.findByName('workflowtesttask').workflowStartDate
		assertNotNull Task.findByName('workflowtesttask').workflowEndDate
	}
}
