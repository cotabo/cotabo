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
		StringBuilder sb = new StringBuilder()
		
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
		StringBuilder sb = new StringBuilder()

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
		def firstWorkflowColumn = board.columns.find{it.workflowStartColumn}
		def lastWorkflowColumn = board.columns.last()
		
		def allWorkflowCollumns = []
		def workflowStartFlag = false		
		board.columns.each {
			//Set flag too true when we reached the workflowStart Column
			if (it.workflowStartColumn)
				workflowStartFlag = true
			//we only use columns from workflowStart to the one before the last column
			//as we don't count the last column to participate in the workflow.
			if(workflowStartFlag && it != board.columns.last())
				allWorkflowCollumns << it		
		}
		//Find all ColumnStatusEntries that reflects the entrance of a task to the workflow		
		def workflowEntryStati = ColumnStatusEntry.findAllEnteredByColumn(firstWorkflowColumn)
		//Find all ColumnstatusEntries for the last column (reflects the end of the workflow)		
		def workflowLeaveStati = ColumnStatusEntry.findAllEnteredByColumn(lastWorkflowColumn)		
		//Add both lists togather (each entry of this will later reclect a single data point)
		def composedList = workflowEntryStati + workflowLeaveStati
		//Sort it by date
		composedList = composedList.sort{a,b-> a.dateCreated <=> b.dateCreated}
		
		StringBuilder sb = new StringBuilder()
		if (composedList) {		
			//Appending 0 as the first value that the graph at the end looks nicer
			sb.append("${composedList.first().dateCreated.time},0\n")
			
			def lastTaskSum
			//For each status entry that we found			
			composedList.each { columnStatusEntry ->				
				def allColumnsEntries = []
				//over all columns				
				allWorkflowCollumns.each {
					//Try to find a corresponding ColumnStatusEntry
					def tmpEntry = getColumnStatusForDate(it, columnStatusEntry.dateCreated)
					if (tmpEntry){
						allColumnsEntries << tmpEntry						
					}
				}					
				//Sum the tasks of all found entries
				def taskSum = allColumnsEntries.sum{it.tasks}
				sb.append("${columnStatusEntry.dateCreated.time},${taskSum}\n")
				//Saving this outside the closure scope so that we have the last value available after iteration
				lastTaskSum = taskSum
			}
			
			//Appending the last value with the current timestamp for better graph look.
			sb.append("${(new Date()).time},${lastTaskSum}\n")
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
