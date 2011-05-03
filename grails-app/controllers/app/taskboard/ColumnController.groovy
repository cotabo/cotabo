package app.taskboard

import java.util.ArrayList;
import grails.converters.*

class ColumnController {
	
	def taskService
	
    def updatetasks = {
		def newTaskOrderIdList = buildSortOrderListFromParam(params['order[]'])
		
		def message =  taskService.moveTask(newTaskOrderIdList, params.fromColumn, 
							params.toColumn, params.taskid)		
		def retCode = message? 1 : 0
		def result = [returncode: retCode, message:message]
		render result as JSON
    }
	
	def updatesortorder = {
		def newTaskOrderIdList = buildSortOrderListFromParam(params['order[]'])

		def message = taskService.updateSortOrder(newTaskOrderIdList)
		
		def retCode = message? 1 : 0
		def result = [returncode: retCode, message:message]
		render result as JSON	
	}
	
	/**
	 * Creates an integer list from the order parameter sent by the
	 * serialized form of tasks in a column.
	 * 
	 * @param orderParam the order parameter from a serialized column
	 * @return an ordered list of task IDs
	 */
	private List buildSortOrderListFromParam(def orderParam) {
		def newTaskOrderIdList = orderParam instanceof String?
			[orderParam]:orderParam as ArrayList
		return newTaskOrderIdList.collect {
			it.split('_')[1].toInteger().intValue()
		}
	}

}
