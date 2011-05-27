package app.taskboard

import grails.test.*

class ColumnStatusEntryTests extends TaskBoardUnitTest {
    protected void setUp() {
        super.setUp()		
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testCreation() {
		def col = Column.findByName('todo')	
		assertNotNull col
		assertNotNull col.tasks	
		def entry = new ColumnStatusEntry(column:col, tasks:col.tasks.clone())
		
		assertTrue entry.validate()
		assertNotNull entry.save()
		assertEquals col.tasks, entry.tasks
    }
	
}
