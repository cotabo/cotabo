package app.taskboard

class ColumnController {
	def sessionFactory
	
    def updatetasks = {
		def newTaskOrderIdList = params['order[]'] instanceof String?
			[params['order[]']]:params['order[]'] as ArrayList
		//Only if we got multiple elements (otherwhise it is injected as String
		newTaskOrderIdList = newTaskOrderIdList.collect {
			it.split('_')[1].toInteger().intValue()
		}
		updateTaskOrder(newTaskOrderIdList)
		
		def fromColumnInstance = Column.get(params.fromColumn)
        def toColumnInstance = Column.get(params.toColumn)
		def taskInstance = Task.get(params.taskid)
		
        if (fromColumnInstance && toColumnInstance && taskInstance) {
			if (params.id != taskInstance.column.id) {
				fromColumnInstance.removeFromTasks(taskInstance)
				toColumnInstance.addToTasks(taskInstance)
				fromColumnInstance.save()
				toColumnInstance.save()
			}
			render(contentType:"text/json") {
				returncode = 0
				message = ''
            }
        }
        else {            
            render(contentType:"text/json") {
				returncode = 1
				message = 'Either the column or the Task that you have specified does not exist.'
            }
        }
		sessionFactory?.getCurrentSession()?.flush()
    }
	
	def updatesortorder = {
		def newTaskOrderIdList = params['order[]'] instanceof String?
			[params['order[]']]:params['order[]'] as ArrayList
		//Only if we got multiple elements (otherwhise it is injected as String
		newTaskOrderIdList = newTaskOrderIdList.collect {
			it.split('_')[1].toInteger().intValue()
		}
		try {
			updateTaskOrder(newTaskOrderIdList)
			sessionFactory?.getCurrentSession()?.flush()
			render(contentType:"text/json") {
				returncode = 0
				message = ''
			}
		}
		catch (Exception e){
			render(contentType:"text/json") {
				returncode = 1
				message = 'Error occured while persisting the data. Please contact the system administrator'
			}
		}
		

	}
	
	private void updateTaskOrder(ArrayList orderedIdList) {
		if(orderedIdList) {
			//Itterate over all tasks in the column where the task was added
			orderedIdList.eachWithIndex { obj, idx ->
				def tmpTask = Task.get(obj)
				//Set the current iteration index as the sort order to maintain
				//as the user sees it.
				tmpTask.sortorder = idx
				if (!tmpTask.save()) {
					flash.message = "Error ordering Task items [$tmpTask]. Entry not valid."
				}
			}
		}
	}
}
