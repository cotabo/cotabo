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
			entry = dashboardService.getColumnStatusForDate(column, startDate + 28.hours)
			assertNotNull entry
			assertEquals ColumnStatusEntry.findByDateCreatedAndColumn(startDate+1.days, column), entry
		}		
    }	
	
	void testGetCDFDataForColumn() {
		
		//our startDate (so the latest one) was 
		def expected = '''1301742793013,1
1301829193013,2
1301915593013,3
1302001993013,4
1302088393013,5
1302174793013,6
1302261193013,7
1302347593013,8
1302433993013,9
1302520393013,10
1302606793013,11
1302693193013,12
1302779593013,13
1302865993013,14
1302952393013,15
1303038793013,16
1303125193013,17
1303211593013,18
1303297993013,19
1303384393013,20
'''		
		assertEquals expected, dashboardService.getCDFDataForColumn(Column.findByName('done'))
	}
	
	void testGetCDFDataForColumnWithDateRestriction() {
		
		//our startDate (so the latest one) was
		def expected = '''1302001993013,4
1302088393013,5
1302174793013,6
1302261193013,7
1302347593013,8
1302433993013,9
1302520393013,10
1302606793013,11
'''
		def from
		def too
		use(TimeCategory) {
			//Including the 3rd day where we had 4 tasks in (as we start with 1 task on the day 0)
			from = startDate + 3.days - 20.seconds			
			//Including the 10th day where we had 11 tasks in
			too = startDate + 10.days + 20.seconds
		}
		assertEquals expected, dashboardService.getCDFDataForColumn(Column.findByName('done'), from, too)
	}
}