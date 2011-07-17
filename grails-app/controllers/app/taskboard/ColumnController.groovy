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
			task:params.taskid?.toInteger()?.intValue(),
			fromColumn: params.fromColumn?.toInteger()?.intValue(),
			toColumn: params.toColumn?.toInteger()?.intValue(),
			newTaskOrderIdList: newTaskOrderIdList
		]		
		//Do the Task moving work
		def resultMessage =  taskService.moveTask(movementMessage)				
		def retCode = resultMessage? 1 : 0
		//Broadcasting also the assignee as this might be updated during 
		//task movements.
		movementMessage.assignee = Task.get(params.taskid?.toInteger()?.intValue()).assignee.toString()
		//Atmosphere stuff - Broadcast this update to the board specific channel
		if (retCode == 0)
			broadcastTaskMovement(movementMessage, 'task_movement')
		//Return code & message will be handled by the client.
		def result = [returncode: retCode, message:resultMessage]
		render result as JSON
    }
	
	/**
	 * This action is called in two situations:
	 * 1.: When a task is moved to an other column in order to update the sort order of the source column
	 * 2.: When a task is moved in the same column (sorting) in order to update the column.
	 */
	def updatesortorder = {
		def newTaskOrderIdList = buildSortOrderListFromParam(params['order[]'])
		if (newTaskOrderIdList) {
			def message = taskService.updateSortOrder(newTaskOrderIdList)		
			def retCode = message? 1 : 0
			if (retCode == 0) {	
				//This means that this actions was called for situation 2. (see comments on action).			
				if(newTaskOrderIdList.contains(params.taskid.toInteger().intValue())) {				
					//Also this is a task movement (only within the same column)
					broadcastTaskMovement(
						task:params.taskid.toInteger().intValue(),
						fromColumn: params.id,
						toColumn: params.id,
						newTaskOrderIdList: newTaskOrderIdList, 
						'task_reordering'
					)
				}
			}
			def result = [returncode: retCode, message:message]
			render result as JSON
		}
		else {
			//Happens e.g. when there this is triggered for source column by
			//moving the last task of a column - just render nothing.			
			render ''
		}
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
		return newTaskOrderIdList?.collect {
			it.split('_')[1].toInteger().intValue()
		}
	}
	
	/**
	 * Distributes the given message to the users registered broadcaster.
	 * 
	 * @param message whatever message should be sent over atmosphere
	 */
	private void broadcastTaskMovement(message, String type) {
		def broadcaster = session.getAttribute("boardBroadacster")?.broadcaster		
		//We just do nothing if there is no broadcaster int he session.
		if (broadcaster) {
			message.type = type
			boardUpdateService.broadcastMessage(message, broadcaster)
		}
	}

}
