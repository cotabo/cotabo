package app.taskboard

import groovy.time.TimeCategory

class DashboardService {

    static transactional = true

    def serviceMethod() {

    }
	
	
	Map overallVsDoneTasks(Date from, Date too, Board, board) {
		from ?:  board.dateCreated
		too ?: new Date()
				
		
	}
	
	/**
	 * Utility method that provides the latest ColumnStatus entry for a given time.
	 * 
	 * @param time
	 * @return
	 */
	ColumnStatusEntry getColumnStatusForTime(Date time) {
		def criteria = ColumnStatusEntry.createCriteria()
		def results = criteria.get {
			le('dateCreated', time)
			maxResults(1)
			order('dateCreated','desc') 
		}
	}
	
}
