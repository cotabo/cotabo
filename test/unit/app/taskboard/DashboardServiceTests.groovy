package app.taskboard

import grails.test.*
import groovy.time.TimeCategory

class DashboardServiceTests extends TaskBoardUnitTest {
	def dashboardService 
    protected void setUp() {
        super.setUp()
		dashboardService = new DashboardService()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testGetColumnStatusForDate() {
		def column = Column.findByName('wip')
		def entry 
		use(TimeCategory) {
			entry = dashboardService.getColumnStatusForDate(column, startDate - 28.hours)
			assertNotNull entry
			assertEquals ColumnStatusEntry.findByDateCreatedAndColumn(startDate-2.days, column), entry
		}
		
    }	
}