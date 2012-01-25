package org.cotabo

import java.util.HashMap.Entry
import groovy.time.TimeCategory

class DashboardService {

    static transactional = true

	/**
	 * Generates CSV data for a cumulative flow diagram for a specific column.
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
		StringBuilder sb = new StringBuilder()
		
		def entries 
		def ordering = [sort:'dateCreated', order:'asc']
		if(!from && ! too) {			
			entries = ColumnStatusEntry.findAllByColumn(column, ordering)
			log.debug "Loading ${entries.size()} ColumnStatusEntries for column ${column.name}"
		}
		else {
			entries = ColumnStatusEntry.findAllByColumnAndDateCreatedBetween(column, from, too, ordering)	
		}
		if(entries) {
			//Starting from 0
			sb << "${entries.first().dateCreated.time},0\n"
			entries.each{
				//As the JavaScript timestamp is in milliseconds we need to multiply by 1000
				sb << "${it.dateCreated.time},${it.tasks}\n"
			}
		}
		return sb.toString()
		
	}
	
	/**
	 * Generates CSV data for the lead-time of all tasks in the last column.	 
	 *
	 * Will look like this:
	 * <milliseconds-since-1970_of_done_date>,<hours>
	 *
	 */
	def getLeadTimeData(Board board, Date from=null, Date to=null) {
		StringBuilder sb = new StringBuilder()

		def entries = getDoneTasks(board, from, to)
		
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
		def resultTasks = getDoneTasks(board, from, too)
		//This is assuming that workflowEndSate & workflowSartDate is always filled when
		//a task is in the last column.
		long sumTime = resultTasks.collect{it.workflowEndDate.time - it.workflowStartDate.time}.sum()
		def result = sumTime / resultTasks.size()
		//we have milliseconds in the result
		def resultHours = result / 60 / 60 / 1000
		return resultHours
	}
	
	private def getDoneTasks(Board board, Date from=null, Date to=null){
		def lastColumn = board.columns.last()
		def ordering = [sort:'workflowEndDate', order:'asc']
		def entries
		if(!from && ! to) {
			//entries = Task.findAllByColumn(lastColumn, ordering)
			entries = lastColumn.tasks
		}
		else {
			//entries = Task.findAllByColumnAndWorkflowEndDateBetween(lastColumn, from, too, ordering)
			entries = lastColumn.tasks.findAll{task -> (from.before(task.workflowEndDate) && task.workflowEndDate.before(to))}
		}
		return entries
	}
	
	/**
	 * Provides CSV data about how much tasks were in the workflow
	 * across either the given time frame or across the whole board 
	 * lifetime.
	 * Will return a data point for each entry & leav of the workflow.
	 * 
	 * As usual in the format:
	 * 	 <milliseconds-since-1970_of_done_date>,<tasks-count>
	 * 
	 * @param board - The board to gather the data for
	 * @return CSV time series data for for tasks in the workflow.
	 */
	def getTaskCountInWorkflowData(Board board) {
		//This method assumes that we capture a ColumnStatus entry for all columns
		//every time a task is moved / created
		boolean workflowStart = false
		def firstColumnIndex = 0
		def columnStatusMap = [:]		
		board.columns.eachWithIndex { item, idx ->
			if(!workflowStart && item.workflowStartColumn) {
				workflowStart = true
				firstColumnIndex = idx
			}
			//Collect all workflow columns in a map
			if(workflowStart) {
				//We don't cound the last column to belong to the workflow
				if(item != board.columns.last()) {
					//Put all ColumnStatusEntries into a map value which key is the column ID					
					columnStatusMap."$idx" = ColumnStatusEntry.findAllByColumn(board.columns[idx], [sort:'dateCreated', order:'asc'])
				}
			}			
		}				
		StringBuilder sb = new StringBuilder()
		//Iterate over all ColumnStatusEntries of the first column
		columnStatusMap."$firstColumnIndex".eachWithIndex { item, idx ->
			//Sum the ColumnStatusEntries with the same index across all column	
			def taskSum = columnStatusMap.collect {key, value -> value[idx].tasks}.sum()
			//Get the time of the current ColumnStatusEntry in the first column
			def time = item.dateCreated.time
			
			sb << "$time,$taskSum\n"
		}
		return sb.toString()
	}
	
	/**
	 * Utility method that provides the latest ColumnStatus entry for a given time.
	 * 
	 * @param time
	 * @return a ColumnStatusEntry object for the given time
	 */
	ColumnStatusEntry getColumnStatusForDate(Column column, Date time) {		
		return ColumnStatusEntry.findByColumnAndDateCreatedLessThanEquals(column, time, [max:1, sort:'dateCreated', order:'desc'])
	}
	
	
	
	
}
