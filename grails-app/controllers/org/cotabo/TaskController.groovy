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
		def taskInstance
		if (params.id) {
			taskInstance = Task.get(params.id.toInteger().integerValue())
			if (!taskInstance) {
				render "Task with id ${params.id} does not exist."
			}
		}				
		else {
			taskInstance = new Task()
		}
		
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
			broadcastTaskCreation(taskInstance.toMessage())
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
        def taskInstance = Task.get(params.id)
        if (taskInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (taskInstance.version > version) {
                    
                    taskInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'task.label', default: 'Task')] as Object[], "Another user has updated this Task while you were editing")
                    render(view: "edit", model: [taskInstance: taskInstance])
                    return
                }
            }
            taskInstance.properties = params
            if (!taskInstance.hasErrors() && taskInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'task.label', default: 'Task'), taskInstance.id])}"
                redirect(action: "show", id: taskInstance.id)
            }
            else {
                render(view: "edit", model: [taskInstance: taskInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'task.label', default: 'Task'), params.id])}"
            redirect(action: "list")
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
	*/
    private void broadcastTaskCreation(message) {
		def broadcaster = session.getAttribute("boardBroadacster")?.broadcaster		
		//We just do nothing if there is no broadcaster int he session.
		if (broadcaster) {
			message.type = 'task_creation'
		   	boardUpdateService.broadcastMessage(message, broadcaster)
		}
    }
}
