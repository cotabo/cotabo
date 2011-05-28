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
		entries.each{
			//As the JavaScript timestamp is in milliseconds we need to multiply by 1000
			sb << "${it.dateCreated.time},${it.tasks.size()}\n"
		}
		return sb.toString()
		
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
