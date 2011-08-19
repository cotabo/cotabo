package org.cotabo

import grails.plugins.springsecurity.Secured
import org.codehaus.groovy.grails.commons.ConfigurationHolder as grailsConfig


@Secured(['ROLE_USER'])
class BoardController {
	def springSecurityService
	def dashboardService
	def boardUpdateService

    static allowedMethods = [save: "POST", update: "POST", delete: "GET"]
	
    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
    	def user = User.findByUsername(springSecurityService.principal.username)						
    	[adminBoards: user.getBoards(RoleEnum.ADMIN), userBoards: user.getBoards(RoleEnum.USER)]
    }

    def create = {
        def boardInstance = new Board()
        boardInstance.properties = params
		
		//Prepare default columns if not already defined
		if (!boardInstance.columns) {
			def columnList = []
			columnList << new Column(name:'Backlog', description:'ToDo backlog', limit: 8)
			columnList << new Column(name:'In Progress', description:'Things that are currently in Progress', limit: 4)
			columnList << new Column(name:'Done!', description:'Things that have already been done')
			boardInstance.columns = columnList
		}		
        return [
			boardInstance: boardInstance, 
			colors:grailsConfig.config.taskboard.colors, 
			priorities:grailsConfig.config.taskboard.priorities,
			users:User.list() 
		]
    }

    def save = {
		//Update an existing instance if id is given
		def boardInstance = params.id ? Board.get(params.id.toInteger()) : new Board()
		
		if (!boardInstance) {
			flash.message = "Board with id ${params.id} does not exist."
			render(status: 404, view: "list")
			return
		}					
		
		//On update
		if(params.id) {
			//Clear all user references for this board
			UserBoard.removeAll(boardInstance)
		}
				
		bindData(boardInstance, params)	
		

		boardInstance.columns[params.workflowStart as int].workflowStartColumn = true

		if (boardInstance.validate() && boardInstance.save(flush:true)){
			//After the board is saved
			//Create a complete new set of relationships between users & boards
			RoleEnum.each { role ->
				List<String> userIdStrings = request.getParameterValues(role.toString()) as ArrayList
				if (userIdStrings) {
					List<Integer> userIds = userIdStrings.collect { it.toInteger() }
					userIds.each { userId ->
						if(userId) {
							def user = User.get(userId)
							println "Associating $user with $boardInstance and role ${role.toString()}"
							UserBoard.create(user, boardInstance, RoleEnum."${role.toString()}", true)
						}
					}
				}
			}
			
			//If no User <> Board relationship created - create at least one admin
			//with the currently logged-in user
			if(!boardInstance.users) {
				def user = User.findByUsername(principal.username)
				UserBoard.create(user, boardInstance, RoleEnum.ADMIN)
			}
	
			//TODO: send out an eMail for all users & admins	
			redirect(action: "show", id: boardInstance.id)			
		}
		else {
			render(view: "create", model: [boardInstance: boardInstance, users: User.list()])
		}
    }

    def show = {
        def boardInstance = Board.read(params.id)
        if (!boardInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'board.label', default: 'Board'), params.id])}"
            redirect(action: "list")
        }
        else {
			[
				boardInstance: boardInstance,
				colors:grailsConfig.config.taskboard.colors,
				priorities:grailsConfig.config.taskboard.priorities
			]			
        }
    }

    def edit = {
        def boardInstance = Board.get(params.id)
        if (!boardInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'board.label', default: 'Board'), params.id])}"
            redirect(action: "list")
        }
        else {
			def allUsers = User.list()			
			//Need to remove already mapped users - Isn't that groovy?			
			allUsers -= boardInstance.users 
            render(view:'create', model:[boardInstance:boardInstance, edit:true, allUsers:allUsers])
        }
    }

    def update = {
        def boardInstance = Board.get(params.id)
        if (boardInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (boardInstance.version > version) {
                    
                    boardInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'board.label', default: 'Board')] as Object[], "Another user has updated this Board while you were editing")
                    render(view: "edit", model: [boardInstance: boardInstance])
                    return
                }
            }
            boardInstance.properties = params
            if (!boardInstance.hasErrors() && boardInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'board.label', default: 'Board'), boardInstance.id])}"
                redirect(action: "show", id: boardInstance.id)
            }
            else {
                render(view: "edit", model: [boardInstance: boardInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'board.label', default: 'Board'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def boardInstance = Board.get(params.id)
        if (boardInstance) {
            try {
				// TODO FIXME remove this hack enabling later board deletion with Board#delete
				boardInstance.columns.each{ column ->
					column.tasks.each{ task ->
						TaskMovementEvent.findAllByTask(task).each { tmEvent ->
							tmEvent.delete(flush:true)
						}
					}
				}
				
				// TODO FIXME remove this hack enabling later board deletion with Board#delete
				boardInstance.columns.each{ column ->
					TaskMovementEvent.findAllByTooColumn(column).each { tmEvent ->
						tmEvent.delete(flush:true)
					}
					
					TaskMovementEvent.findAllByFromColumn(column).each { tmEvent ->
						tmEvent.delete(flush:true)
					}
				}
				
				// TODO FIXME remove this hack enabling later board deletion with Board#delete
				boardInstance.columns.each{ column ->
					ColumnStatusEntry.findAllByColumn(column).each { csEntry ->
						csEntry.delete(flush:true)
					}
				}
				
				// TODO FIXME remove this hack enabling later board deletion with Board#delete
				UserBoard.findAllByBoard(boardInstance).each { userboard -> userboard.delete(flush:true) }
				
                boardInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'board.label', default: 'Board'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'board.label', default: 'Board'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'board.label', default: 'Board'), params.id])}"
            redirect(action: "list")
        }
    }
	
	def chat = {
		def message = params.message.encodeAsHTML()
		if (message) {
			def user = User.findByUsername(springSecurityService.principal.username)			
			boardUpdateService.broadcastMessage (
				session.getAttribute("boardBroadacster")?.broadcaster,
				[chat_user:user.toString(), chat_message: message],
				MessageType.CHAT_MESSAGE
			)
		}
		render ''				
	}

	/**
	 * Helper method that takes an array based request parameter
	 * of user IDs and returns a list of User objects.
	 * 
	 * @param param - array based request parameter of user IDs
	 * @return List of user objects
	 */
	public List<User> buildUserListFromParam(def param) {		
		def idList
		if(param instanceof String) {
			idList = [param.toInteger()]
		} 
		else {
			def tmpList =  param as ArrayList
			idList = tmpList.collect {it.toInteger()}
		}		
		return idList.collect {User.get(it)}		
	}
	
	def comulativeflowchart = {		
		def boardInstance = Board.get(params.id)
		if(!boardInstance) {
			flash.message = "Board with it [${params.id}] not found"
		}
		[boardInstance: boardInstance]
	}	
	def getCdfDataForColumn = {
		def columnInstance = Column.get(params.id)
		if (!columnInstance) {
			flash.message = "Column ID does not exist"
			render flash.message
		}
		def result = dashboardService.getCDFDataForColumn(columnInstance, params.from, params.too)
		render result
	}
	
	def leadtimechart = {
		def boardInstance = Board.get(params.id)
		if(!boardInstance) {
			flash.message = "Board with it [${params.id}] not found"
		}
		[boardInstance: boardInstance]
	}		
	def getLeadTimeData = {
		def boardInstance = Board.get(params.id)
		if(!boardInstance) {
			flash.message = "Board ID [${params.id}] does not exist."
			render flash.message
		}
		def result = dashboardService.getLeadTimeData(boardInstance, params.from, params.too)
		render result
	}	
	def getWorkflowTaskAmountData = {
		def boardInstance = Board.get(params.id)
		if(!boardInstance) {
			flash.message = "Board ID [${params.id}] does not exist."
			render flash.message
		}
		def result = dashboardService.getTaskCountInWorkflowData(boardInstance)
		render result
	}
	def getAverageCycleTime = {
		def boardInstance = Board.get(params.id)
		if (!boardInstance) {
			flash.message = "Board ID [${params.id}] does not exist."
			render flash.message
		}
		def result = dashboardService.getAverageCycleTime(boardInstance, params.from, params.too)
		render result
	}	
	
}
