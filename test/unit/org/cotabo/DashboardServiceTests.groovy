package org.cotabo

import grails.test.*
import groovy.time.TimeCategory

class DashboardServiceTests extends TaskBoardUnitTest {
	def dashboardService 
    protected void setUp() {
        super.setUp()
		mockLogging(DashboardService)
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
		
	   //  1*0 as start
	   //      20*0 (create)
	   //      1*0 (1st move too wip)
	   //  2*x (2nd move too done + 1st move too wip of next task)
	   //There are is also 2*20 in between as we are creating 2 additional tasks on startDate
	   //in the superclass
	   def expected = '''1301742793013,0
1301742793013,0
1301742793013,0
1301742793013,0
1301742793013,0
1301742793013,0
1301742793013,0
1301742793013,0
1301742793013,0
1301742793013,0
1301742793013,0
1301742793013,0
1301742793013,0
1301742793013,0
1301742793013,0
1301742793013,0
1301742793013,0
1301742793013,0
1301742793013,0
1301742793013,0
1301742793013,0
1301742793013,0
1301742793013,20
1301742793013,20
1301757193013,1
1301829193013,1
1301843593013,2
1301915593013,2
1301929993013,3
1302001993013,3
1302016393013,4
1302088393013,4
1302102793013,5
1302174793013,5
1302189193013,6
1302261193013,6
1302275593013,7
1302347593013,7
1302361993013,8
1302433993013,8
1302448393013,9
1302520393013,9
1302534793013,10
1302606793013,10
1302621193013,11
1302693193013,11
1302707593013,12
1302779593013,12
1302793993013,13
1302865993013,13
1302880393013,14
1302952393013,14
1302966793013,15
1303038793013,15
1303053193013,16
1303125193013,16
1303139593013,17
1303211593013,17
1303225993013,18
1303297993013,18
1303312393013,19
1303384393013,19
1303398793013,20
'''
		def result = dashboardService.getCDFDataForColumn(Column.findByName('done'))
		def resultList = result.readLines()
		
		assertEquals expected.readLines(), resultList
	}
	
	void testGetCDFDataForColumnWithDateRestriction() {
		
		//our startDate (so the latest one) was
		def expected = '''1302001993013,0
1302001993013,3
1302016393013,4
1302088393013,4
1302102793013,5
1302174793013,5
1302189193013,6
1302261193013,6
1302275593013,7
1302347593013,7
1302361993013,8
1302433993013,8
1302448393013,9
1302520393013,9
1302534793013,10
1302606793013,10
1302621193013,11
'''
		def from
		def too
		use(TimeCategory) {
			//Including the 3rd day where we had 4 tasks in wip (as we start with 1 task on the day 0)
			from = startDate + 3.days - 20.seconds			
			//Including the 10th day where we had 10 tasks in Done
			too = startDate + 10.days + 4.hours + 20.seconds
		}
		def result = dashboardService.getCDFDataForColumn(Column.findByName('done'), from, too)
		def resultList = result.readLines()
		
		//Comparing both line collections		
		assertEquals expected.readLines(), resultList
	}
	
	void testGetAverageCycleTime() {
		def board = Board.findByName('myboard')
		//Our workflowStartColumn is 'wip' and every tasks needs 4 hours to be handled
		long expected = 4
		assertEquals expected, dashboardService.getAverageCycleTime(board)
		
	}
	
	void testGetLeadTimeData() {
		//In TEST environment we set everything to the same timestamp
		//and always expect 4 hours lead-time.
		//see Task.beforeUpdate & Task.beforeInsert
		def expected = '''1301757193013,4
1301757193013,4
1301757193013,4
1301757193013,4
1301757193013,4
1301757193013,4
1301757193013,4
1301757193013,4
1301757193013,4
1301757193013,4
1301757193013,4
1301757193013,4
1301757193013,4
1301757193013,4
1301757193013,4
1301757193013,4
1301757193013,4
1301757193013,4
1301757193013,4
1301757193013,4
'''
		def board = Board.findByName('myboard')
		assertEquals expected, dashboardService.getLeadTimeData(board)
	}
	
	void testGetLeadTimeDataWithDateRestrictions() {
		//In TEST environment we set everything to the same timestamp
		//and always expect 4 hours lead-time.
		//see Task.beforeUpdate & Task.beforeInsert
		def expected = '''1301757193013,4
1301757193013,4
1301757193013,4
1301757193013,4
1301757193013,4
1301757193013,4
1301757193013,4
1301757193013,4
1301757193013,4
1301757193013,4
1301757193013,4
1301757193013,4
1301757193013,4
1301757193013,4
1301757193013,4
1301757193013,4
1301757193013,4
1301757193013,4
1301757193013,4
1301757193013,4
'''
		def from
		def too
		use(TimeCategory) {
			//from the beginning
			from = startDate 		
			//Until +4 hours & 20 seconds - this should include everything 
			//as everything happend within this time-frame in TEST env.
			too = startDate + 4.hours + 20.seconds
		}
		
		def board = Board.findByName('myboard')
		assertEquals expected, dashboardService.getLeadTimeData(board, from, too)
	}
	
	void testGetTaskCountInWorkflowData() {
		//Building the expected
		StringBuilder sb = new StringBuilder()		
		//20 creations in todo
		20.times {
			sb.append("${startDate.time},0\n")
		}
		//Two additional tasks are created (1 in wip and 1 in todo) at startDate
		sb.append("${startDate.time},1\n")
		sb.append("${startDate.time},0\n")		
		//20 tasks are moved along the workflow
		for(i in 0..19) {
			use(TimeCategory) {
				//See TaskBoardUnitTest - we're moving always one task to wip
				//and 4 hours later too done.
				sb.append("${(startDate + i.days).time},1\n")
				sb.append("${((startDate + i.days)+4.hours).time},0\n")				
			}
		}
			
		def expected = sb.toString()
		
		//We need to pop the last element as this will be the current timestamp (not testable)
		def board = Board.findByName('myboard')
		assertNotNull board		
		
		assertTrue board.columns?.size() > 0		
		
		def result = dashboardService.getTaskCountInWorkflowData(board)
		assertNotNull result		
		
		def resultList = result.readLines()
		assertNotNull resultList		
		assertTrue resultList?.size() > 0								
		assertEquals expected.readLines(), resultList
	}
}
