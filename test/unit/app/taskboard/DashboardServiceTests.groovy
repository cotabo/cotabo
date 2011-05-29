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
			entry = dashboardService.getColumnStatusForDate(column, startDate + 27.hours)
			assertNotNull entry
			assertEquals ColumnStatusEntry.findByDateCreatedAndColumn(startDate+1.days, column), entry
		}		
    }	
	
	void testGetCDFDataForColumn() {
		
		//our startDate (so the latest one) was 
		def expected = '''1301757193013,1
1301843593013,2
1301929993013,3
1302016393013,4
1302102793013,5
1302189193013,6
1302275593013,7
1302361993013,8
1302448393013,9
1302534793013,10
1302621193013,11
1302707593013,12
1302793993013,13
1302880393013,14
1302966793013,15
1303053193013,16
1303139593013,17
1303225993013,18
1303312393013,19
1303398793013,20
'''	
		
		assertEquals expected, dashboardService.getCDFDataForColumn(Column.findByName('done'))
	}
	
	void testGetCDFDataForColumnWithDateRestriction() {
		
		//our startDate (so the latest one) was
		def expected = '''1302016393013,4
1302102793013,5
1302189193013,6
1302275593013,7
1302361993013,8
1302448393013,9
1302534793013,10
1302621193013,11
'''
		def from
		def too
		use(TimeCategory) {
			//Including the 3rd day where we had 4 tasks in (as we start with 1 task on the day 0)
			from = startDate + 3.days - 20.seconds			
			//Including the 10th day where we had 11 tasks in
			too = startDate + 10.days + 4.hours + 20.seconds
		}
		assertEquals expected, dashboardService.getCDFDataForColumn(Column.findByName('done'), from, too)
	}
}