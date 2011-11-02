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
		//Do the Task moving work
		def resultMessage =  taskService.moveTask(fromColumn, toColumn, task)
		def retCode = resultMessage? 1 : 0
		//Distribute this movement by rerendering the 2 columns
		def user = User.findByUsername(springSecurityService.principal.username)
		def broadcaster = session.getAttribute("boardBroadacster")?.broadcaster		
		def notification = "${user} moved '${task.name}' (#${task.id}) to '${toColumn}'"
		
		boardUpdateService.broadcastRerenderingMessage(broadcaster, fromColumn, notification)
		boardUpdateService.broadcastRerenderingMessage(broadcaster, toColumn)
		
		//Return code & message will be handled by the client.
		def result = [returncode: retCode, message:resultMessage]
		render result as JSON
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
			def broadcaster = session.getAttribute("boardBroadacster")?.broadcaster
			boardUpdateService.broadcastRerenderingMessage(broadcaster, taskInstance, notification)
			//Render nothing as this will be done by atmosphere
			render ''
        }
        else {						
			withFormat {
				form {							
					//We render all errors on the client side
					def resp = [
						title: 'Task could not be saved', 
						message: taskInstance.errors.allErrors.join('\n') 
					]
					render(status: 500, contentType:'application/json', text: resp as JSON)
				}
			}	            
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
				def broadcaster = session.getAttribute("boardBroadacster")?.broadcaster
				def notification
				//distinguishing messages between block updates and normal updates
				if(settedBlock) {
					notification = "${user} marked task #${params.id} (${taskInstance.name}) as ${wasBlocked ? 'unblocked' : 'blocked'}."
				}
				else {
					notification = "${user} updated task #${params.id} (${taskInstance.name})."
				}
				boardUpdateService.broadcastRerenderingMessage(broadcaster, taskInstance, notification)
                render ''
            }
            else {
				def message = [
					title: "Error updating task ${params.id}",
					message: taskInstance.errors.allErrors.join('\n') 
				]
				render(status: 500, contentType:'application/json', text: message as JSON)				
            }
        }
        else {
			def message = [
				title: 'Task not found',
				message: "Task with id ${params.id} does not exist."
			]
			render(status: 404, contentType:'application/json', text: message as JSON)            
        }
    }

    def delete = {
        def taskInstance = Task.get(params.id)
        if (taskInstance) {
            try {
                taskInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'task.label', default: 'Task'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'task.label', default: 'Task'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'task.label', default: 'Task'), params.id])}"
            redirect(action: "list")
        }
    }	
	
	def edit = {
		def taskInstance = Task.get(params.id)
		if (taskInstance) {
			render(template: 'edit', model:[taskInstance:taskInstance])
		}
		else {
			def resp = [title: 'Task not found', message: "Task with id ${params.id} does not exist." ]
			render (status: 404, contentType:'application/json', text: resp as JSON)
		}
	}
	
	def archive = {
		def taskInstance = Task.get(params.id)
		log.debug("Archiving task ${taskInstance}...")
		if( taskInstance) {
			taskInstance.archived = true;
			taskInstance.save(flush:true);
			def broadcaster = session.getAttribute("boardBroadacster")?.broadcaster
			boardUpdateService.broadcastRerenderingMessage(broadcaster, taskInstance)
		}
		redirect(controller :'board', action: 'show', id:taskInstance.column.board.id)
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
}
