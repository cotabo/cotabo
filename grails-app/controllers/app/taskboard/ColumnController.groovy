package app.taskboard

class ColumnController {
    
    def updatetasks = {
		//Only if we got multiple elements (otherwhise it is injected as String
		if (!params['order[]'] instanceof String ) {
			def newTaskOrderIdList = params['order[]'].collect {
				it.split('_')[1].toInteger().intValue()
			}
		}
		//TODO Itterate over the newTaskOrderIdList and get the corresponding tasks
		//and assign them the Iteration index as the new sort order.
		
		def fromColumnInstance = Column.get(params.fromColumn)
        def toColumnInstance = Column.get(params.toColumn)
		def taskInstance = Task.get(params.taskid)
        if (fromColumnInstance && toColumnInstance && taskInstance) {
			if (params.id != taskInstance.column.id) {
				fromColumnInstance.removeFromTasks(taskInstance)
				toColumnInstance.addToTasks(taskInstance)
				fromColumnInstance.save()
				toColumnInstance.save(flush:true)
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
    }
}
