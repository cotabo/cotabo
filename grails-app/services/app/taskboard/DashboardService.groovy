package app.taskboard

import groovy.time.TimeCategory

class DashboardService {

    static transactional = true

	/**
	 * Generates CSV data for a comulative flow diagram for a specific column.
	 * 
	 * Will look like this:
	 * <milliseconds-since-1970>,<numer of tasks>
	 * 
	 * 
	 * 
	 * @param board The board that you want to have the data for
	 * @param from From when you want to have the data
	 * @param too the time that you want to have the data
	 * @return
	 */
	def getCDFDataForColumn(Column column, Date from=null, Date too=null) {
		StringBuffer sb = new StringBuffer()
		
		def entries 
		def ordering = [sort:'dateCreated', order:'asc']
		if(!from && ! too) {
			entries = ColumnStatusEntry.findAllByColumn(column, ordering)
		}
		else {
			entries = ColumnStatusEntry.findAllByColumnAndDateCreatedBetween(column, from, too, ordering)	
		}
		sb << "${entries.first().dateCreated.time},0\n"
		entries.each{
			//As the JavaScript timestamp is in milliseconds we need to multiply by 1000
			sb << "${it.dateCreated.time},${it.tasks}\n"
		}
		sb << "${new Date().time},${entries.last().tasks}\n"
		return sb.toString()
		
	}
	
	/**
	 * Generates CSV data for the lead-time of all tasks in the last column.	 
	 *
	 * Will look like this:
	 * <milliseconds-since-1970_of_done_date>,<hours>
	 *
	 */
	def getLeadTimeData(Board board, Date from=null, Date too=null) {
		StringBuffer sb = new StringBuffer()

		def entries
		def ordering = [sort:'workflowEndDate', order:'asc']
		def lastColumn = board.columns.last()
		if(!from && ! too) {
			entries = Task.findAllByColumn(lastColumn, ordering)
		}
		else {
			entries = Task.findAllByColumnAndWorkflowEndDateBetween(lastColumn, from, too, ordering)	
		}
		
		def hoursDate
		entries.each { task ->			
			use(TimeCategory) {			
				hoursDate = task.workflowEndDate - task.workflowStartDate
				sb << "${task.workflowEndDate.time},${hoursDate.hours}\n"
			}
		}
		return sb.toString()
	}


	/**
	 * Returns the average thoughput time of tasks though the board
	 * in milliseconds. If no range (from & too) is given it will be calculated
	 * for the whole time that the board exists.
	 * 
	 * @param board The board that you want to have the data for
	 * @param from [optional] from when
	 * @param too [optional] too when
	 * @return the average thoughput time in miliseconds
	 */
	long getAverageCycleTime(Board board, Date from=null, Date too=null) {
		def workflowEndColumn = board.columns.last()
		def resultTasks 
		if (from && too) {
			resultTasks = Task.findAllByColumnAndWorkflowEndDateBetween(workflowEndColumn, from, too)
		}
		else {
			resultTasks = Task.findAllByColumnAndWorkflowEndDateIsNotNull(workflowEndColumn)
		}
		//This is assuming that workflowEndSate & workflowSartDate is always filles when
		//a task is in the last column.
		long sumTime = resultTasks.collect{it.workflowEndDate.time - it.workflowStartDate.time}.sum()
		def result = sumTime / resultTasks.size()
		//we have milliseconds in the result
		def resultHours = result / 60 / 60 / 1000
		return resultHours
	}
	
	/**
	 * Utility method that provides the latest ColumnStatus entry for a given time.
	 * 
	 * @param time
	 * @return
	 */
	ColumnStatusEntry getColumnStatusForDate(Column column, Date time) {		
		return ColumnStatusEntry.findByColumnAndDateCreatedLessThanEquals(column, time, [max:1, sort:'dateCreated', order:'desc'])
	}
	
	
	
}
