package org.cotabo

import grails.test.*


class AutoArchiveDoneJobTests extends TaskBoardUnitTest {
    protected void setUp() {
        super.setUp()
		mockConfig '''
		taskboard.colors = ['#fafaa8', '#faaca8', '#85fd81', '#81b6fd']
		taskboard.default.colors = '#f9f21a'
		taskboard.priorities = ['Critical', 'Major', 'Normal', 'Low']
		taskboard.default.priority = 'Normal'
		taskboard.default.autoarchive = 30 
		'''
		
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testArchive() {
		//Preparation
		def col = Column.findByName('done')
		def user = User.findByUsername('testuser')
		assertNotNull col
		assertNotNull user
		
		def task = new Task(
			name: 'mytask',
			durationHours: 0.5,
			column: col,
			creator: user,			
			color: new TaskColor(color:'#faf77a', name:'none'),
			priority: 'Critical',
			lastUpdated: new Date().minus(31)
		)
		task.validate()
		task.errors.allErrors.each {println it}
		assertTrue task.validate()
		assertNotNull task.save()
		
		task = Task.findByName('mytask')
		assertNotNull task
		task.lastUpdated = new Date().minus(31)
		
		def job = new AutoArchiveDoneJob()
		job.execute()
		assertNotNull task.archived
    }
}
