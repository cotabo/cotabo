package org.cotabo

import grails.test.*


class AutoArchiveDoneJobTests extends TaskBoardUnitTest {
    protected void setUp() {
        super.setUp()
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
			sortorder: 1,
			color: new TaskColor(color:'#faf77a', name:'none'),
			priority: 'Critical',
			lastUpdated: new Date().minus(31)
		)
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
