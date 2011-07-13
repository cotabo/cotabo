package app.taskboard

import java.util.ArrayList;
import grails.converters.JSON

class ColumnController {
	
	def taskService
	def boardUpdateService
	
	
    def updatetasks = {
		//Prepare the movement message
		def newTaskOrderIdList = buildSortOrderListFromParam(params['order[]'])
		def movementMessage = [
			task:params.taskid.toInteger().intValue(),
			fromColumn: params.fromColumn.toInteger().intValue(),
			toColumn: params.toColumn.toInteger().intValue(),
			newTaskOrderIdList: newTaskOrderIdList
		]		
		//Do the Task moving work
		def resultMessage =  taskService.moveTask(movementMessage)				
		def retCode = resultMessage? 1 : 0
		//Atmosphere stuff - Broadcast this update to the board specific channel
		if (retCode == 0)
			broadcastTaskMovement(movementMessage)
		//Return code & message will be handled by the client.
		def result = [returncode: retCode, message:resultMessage]
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
	
	/**
	 * Distributes the given message to the users registered broadcaster.
	 * 
	 * @param message Something that can be converted to JSON
	 */
	private void broadcastTaskMovement(message) {
		def broadcaster = session.getAttribute("boardBroadacster")?.broadcaster		
		//We just do nothing if there is no broadcaster int he session.
		if (broadcaster) {
			message.type = 'task_movement'
			boardUpdateService.broadcastMessage(message, broadcaster)
		}
	}

}
