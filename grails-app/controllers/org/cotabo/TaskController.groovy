package org.cotabo

import grails.converters.*

class TaskController {
	def springSecurityService
	def taskService
	def boardUpdateService
		
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [taskInstanceList: Task.list(params), taskInstanceTotal: Task.count()]
    }

    def create = {
        def taskInstance = new Task()
        taskInstance.properties = params
        return [taskInstance: taskInstance]
    }
	
	def move = {
		def fromColumn = Column.get(params.fromColumn)
		def toColumn = Column.get(params.toColumn)  
		def task = Task.get(params.taskid)
		if (task && fromColumn && toColumn) {
			//Do the Task moving work
			taskService.moveTask(fromColumn, toColumn, task, params.toIndex as int)			
			//Distribute this movement by rerendering the 2 columns
			def user = User.findByUsername(springSecurityService.principal.username)
			def broadcaster = session.getAttribute("boardBroadcaster")?.broadcaster		
			def notification = "${user} moved '${task.name}' (#${task.id}) to '${toColumn}'"	

			boardUpdateService.broadcastRerenderingMessage(broadcaster, [fromColumn, toColumn])				
			render ''
		}
		else {
			sendError("One of the following objects does not exist: column: ${params.fromColumn}, column: ${params.toColumn}, task: ${params.taskid}",
				404)
		}
	}
	
	def reorder = {		
		def task = Task.get(params.id)
		def position = params.position as int
		if (task && position > -1) {
			taskService.reorderTask(task, position)
			def broadcaster = session.getAttribute("boardBroadcaster")?.broadcaster
			boardUpdateService.broadcastRerenderingMessage(broadcaster, task.column)
			render ''
		}
		else {
			sendError("Task does not exist", "The task #${params.id} does not exist", 404)
		}	
		
	}
	
    def save = {		
		def taskInstance = new Task()
				
		//Bind data but exclude column, creator
		bindData(taskInstance, params, ['column','creator', 'assignee'])
		
		//Binding for colors
		bindColor(taskInstance, params.color)
		
		taskInstance.column = Board.get(params.board).columns.first()

		//Assign the creator
		def creator = User.findByUsername(springSecurityService.principal.username)
		taskInstance.creator = creator
		
		def assignee = User.get(params.assignee.trim())
		//No check on assignee as this may be null - leave this to the constraints
		taskInstance.assignee =assignee				

		taskInstance = taskService.saveTask(taskInstance)
		
		def user = User.findByUsername(springSecurityService.principal.username)
        if (!taskInstance.hasErrors()) {
			def notification = "${user} created '${taskInstance.name}' (#${taskInstance.id})."
			def broadcaster = session.getAttribute("boardBroadcaster")?.broadcaster
			boardUpdateService.broadcastRerenderingMessage(broadcaster, taskInstance)
			//Render nothing as this will be done by atmosphere
			render ''
        }
        else {
			sendError(renderErrors(bean:taskInstance).toString(), 500)			           
        }
    }

    def update = {
        def taskInstance = Task.get(params.id.toLong())
        if (taskInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (taskInstance.version > version) {
                    
                    taskInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'task.label', default: 'Task')] as Object[], "Another user has updated this Task while you were editing")
                    render(view: "edit", model: [taskInstance: taskInstance])
                    return
                }
            }
						
			boolean settedBlock = false 			
			boolean wasBlocked = params.wasBlocked?.toBoolean()?.booleanValue()
			//If we have a 'wasBlocked' param which equals to the current task status
			boolean currentBlocked = taskInstance.blocked
            if(currentBlocked == wasBlocked) {			
				//Flip the blocked status	
				taskInstance.blocked = !wasBlocked	
				//saving that this was a blocked status update
				settedBlock = true
			}
			//else it is a normal update on the task and we bind everything necessary
			else {
				//Bind data but exclude column, creator
				bindData(taskInstance, params, ['column','creator', 'assignee'])
				//Binding for colors
				bindColor(taskInstance, params.color)				
				def assignee = User.get(params.assignee?.trim()?.toLong())
				//No check on assignee as this may be null - leave this to the constraints
				taskInstance.assignee =assignee
			}
			
            if (!taskInstance.hasErrors() && taskInstance.save(flush: true)) {
				def user = User.findByUsername(springSecurityService.principal.username)
				def broadcaster = session.getAttribute("boardBroadcaster")?.broadcaster
				def notification
				//distinguishing messages between block updates and normal updates
				if(settedBlock) {
					notification = "${user} marked task #${params.id} (${taskInstance.name}) as ${wasBlocked ? 'unblocked' : 'blocked'}."
				}
				else {
					notification = "${user} updated task #${params.id} (${taskInstance.name})."
				}
				boardUpdateService.broadcastRerenderingMessage(broadcaster, taskInstance)
                render ''
            }
            else {
				sendError(renderErrors(bean:taskInstance).toString(), 500)	
            }
        }
        else {
			sendError("Task with id ${params.id} does not exist.", 404)       
        }
    }
		
	def edit = {
		def taskInstance = Task.get(params.id)
		if (taskInstance) {
			render(template: 'edit', model:[taskInstance:taskInstance])
		}
		else {
			sendError("Task with id ${params.id} does not exist.", 404)			
		}
	}
	
	def archive = {
		def taskInstance = Task.get(params.id)
		if( taskInstance) {
			taskInstance.archived = true;
			taskInstance.save(flush:true);
			def broadcaster = session.getAttribute("boardBroadcaster")?.broadcaster
			boardUpdateService.broadcastRerenderingMessage(broadcaster, taskInstance.column)
			boardUpdateService.broadcastRerenderingMessage(broadcaster, taskInstance, 'showarchived')
		}		
		redirect(controller :'board', action: 'show', id:taskInstance.column.board.id)
	}
	
	def unarchive = {
		def taskInstance = Task.get(params.id)
		if( taskInstance) {
			taskInstance.archived = false;
			taskInstance.save(flush:true);
			def broadcaster = session.getAttribute("boardBroadcaster")?.broadcaster
			boardUpdateService.broadcastRerenderingMessage(broadcaster, taskInstance.column)
			boardUpdateService.broadcastRerenderingMessage(broadcaster, taskInstance, 'showarchived')
		}
		redirect(controller :'board', action: 'archive', id:taskInstance.column.board.id)
	}
	
	def showarchived = {
		def taskInstance = Task.get(params.id)
		if (taskInstance) {
			render(template: 'showarchived', model:[taskInstance:taskInstance])
		}
	}
	
	def showDom = {
		def taskInstance = Task.get(params.id)
		if (taskInstance) {
			render(template: 'show', model:[taskInstance:taskInstance])
		}
	}
	
	/**
	 * Helper method that takes care of color binding to a task instance from a requested list of color codes.
	 * 
	 * @param taskInstance The task object
	 * @param colors either a string array representation([#ffffff, #ffff00]) of hex codes or a single one.
	 */
	private void bindColor(Task taskInstance, def colors) {		
		//make it a list if it's a string (in case that only one is selected)
		colors = colors instanceof String ? [colors] : colors
		//Clearing the colors
		taskInstance.colors?.clear()										
		//Than adding everything as requested
		colors?.each { reqColor ->
			taskInstance.addToColors(TaskColor.findByColor(reqColor))
		}
	}
	
	/**
	 * Renders am error message in the defines format that the client understands.
	 * 
	 * @param title the title of the error message
	 * @param message the detailes message
	 * @param status the HTTP status code (default = 500)
	 */
	private void sendError(String message, int status = 500) {
		render(status: status, template: '/error', model:[message: message])
	}
}
