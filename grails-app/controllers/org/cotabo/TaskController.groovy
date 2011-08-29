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

    def save = {
		def taskInstance = new Task()
				
		//Bind data but excluse column, creator & sortorder
		bindData(taskInstance, params, ['column','creator','sortorder', 'assignee'])
		
		taskInstance.column = Board.get(params.board).columns.first()
		//Render error when the user is not logged in
		if(!springSecurityService.isLoggedIn()) {
			def resp = [title: 'No user session', message: 'You\'r not logged in.\nPlease refresh the site to re-login.' ]
			render (status: 403, contentType:'application/json', text: resp as JSON)
			return
		}
		//Assign the creator
		def creator = User.findByUsername(springSecurityService.principal.username)
		taskInstance.creator = creator
		
		def assignee = User.get(params.assignee.trim())
		//No check on assignee as this may be null - leave this to the constraints
		taskInstance.assignee =assignee		
		
		//Get the highest sortorder of the current column + 1
		def sortOrder = Task.createCriteria().get {
			eq("column", taskInstance.column)
			projections {
				max("sortorder")
			}
		} 		
		//Set it to 1 on the first task
		taskInstance.sortorder = sortOrder ? sortOrder+1 : 1

		taskInstance = taskService.saveTask(taskInstance)

        if (!taskInstance.hasErrors()) {
			def principal = springSecurityService.principal
			def user = User.findByUsername(principal.username)
			def notification = "${user} created task #${taskInstance.id} (${taskInstance.name})."
			def broadcaster = session.getAttribute("boardBroadacster")?.broadcaster
			//Distribute this creation as atmosphere message
			boardUpdateService.broadcastMessage(
				broadcaster, 
				taskInstance.toMessage(), 
				MessageType.TASK_CREATION, 
				notification
			)
			
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
				//Bind data but exclude column, creator & sortorder
				bindData(taskInstance, params, ['column','creator','sortorder', 'assignee'])				
				def assignee = User.get(params.assignee.trim().toLong())
				//No check on assignee as this may be null - leave this to the constraints
				taskInstance.assignee =assignee
			}
			
            if (!taskInstance.hasErrors() && taskInstance.save(flush: true)) {
				def principal = springSecurityService.principal
				def user = User.findByUsername(principal.username)				
				def broadcaster = session.getAttribute("boardBroadacster")?.broadcaster
				//distinguishing messages between block updates and normal updates
				if(settedBlock) {
					def notification = "${user} marked task #${params.id} (${taskInstance.name}) as ${wasBlocked ? 'unblocked' : 'blocked'}."
					def block_message = [task:taskInstance.id, blocked:!wasBlocked]
					boardUpdateService.broadcastMessage(
						broadcaster,
						block_message,
						MessageType.TASK_BLOCK,
						notification
					)
				}
				else {
					def notification = "${user} updated task #${params.id} (${taskInstance.name})."
					boardUpdateService.broadcastMessage(
						broadcaster,
						taskInstance.toMessage(), 
						MessageType.TASK_UPDATE,
						notification
					)
				}
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
			
			boardUpdateService.broadcastMessage(
				session.getAttribute("boardBroadacster")?.broadcaster,
				taskInstance.toMessage(),
				MessageType.ALL,
				''
			)
		}
		redirect(controller :'board', action: 'show', id:taskInstance.column.board.id)
	}
}
