package app.taskboard

class ColumnController {
    

    def updatetasks = {
        def columnInstance = Column.get(params.id)
		def taskInstance = Task.get(params.taskid)
        if (columnInstance && taskInstance) {
            columnInstance.addToTasks(taskInstance)
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
