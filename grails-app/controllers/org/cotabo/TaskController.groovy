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
				
		//Bind data but excluse column, creator, assignee & sortorder
		bindData(taskInstance, params, ['column','creator','assignee','sortorder'])
		
		taskInstance.column = Board.get(params.board).columns.first()
		taskInstance.creator = User.findByUsername(springSecurityService.principal.username)
		if (params.assignee && params.assignee.trim() != '') {
			try {
				taskInstance.assignee = User.get(params.assignee)
			}
			catch (org.hibernate.TypeMismatchException e) {
				render e.message
			}
		}
		
		
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
			//Distribute this creation as atmosphere message
			broadcastMessage (taskInstance.toMessage(), 'task_creation')
			
			//Render nothing as this will be done by atmosphere
			render ''     
        }
        else {						
			withFormat {
				form {							
					//We render all errors on the client side
					render taskInstance.errors as JSON
				}
			}	            
        }
    }

    def show = {
        def taskInstance = Task.get(params.id)
        if (!taskInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'task.label', default: 'Task'), params.id])}"
            redirect(action: "list")
        }
        else {
            [taskInstance: taskInstance]
        }
    }

    def edit = {
        def taskInstance = Task.get(params.id)
        if (!taskInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'task.label', default: 'Task'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [taskInstance: taskInstance]
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
            if(params.wasBlocked && (taskInstance.blocked == wasBlocked)) {			
				//Flip the blocked status	
				taskInstance.blocked = !wasBlocked	
				//saving that this was a blocked status update
				settedBlock = true
			}
			//else it is a normal update on the task and we bind everything necessary
			else {
				//Bind data but excluse column, creator, assignee & sortorder
				bindData(taskInstance, params, ['column','creator','assignee','sortorder'])
			}
			
            if (!taskInstance.hasErrors() && taskInstance.save(flush: true)) {
				//distinguishing messages between block updates and normal updates
				if(settedBlock) {
					def block_message = [task:taskInstance.id, blocked:!wasBlocked]
					broadcastMessage(block_message, 'task_block')
				}
				else {
					broadcastMessage(taskInstance.toMessage(), 'task_update')
				}
                render ''
            }
            else {
				//TODO: fix this - sending a common JSON piece back when something went wrong
				//and react and the error callback on the client for this request.
				render taskInstance.errors as JSON				
                //render(view: "edit", model: [taskInstance: taskInstance])
            }
        }
        else {
			//TODO:  fix this - sending a common JSON piece back when task wasn't found
			render 'ERROR - task not found'
            //flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'task.label', default: 'Task'), params.id])}"
            
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
	
	/**
	* Distributes the given message to the users registered broadcaster.
	*
	* @param message Something that can be converted to JSON
	* @param type the type string that will be used in the client code
	*/
    private void broadcastMessage(message, type) {
		def broadcaster = session.getAttribute("boardBroadacster")?.broadcaster		
		//We just do nothing if there is no broadcaster int he session.
		if (broadcaster) {
			message.type = type
		   	boardUpdateService.broadcastMessage(message, broadcaster)
		}
    }
}
