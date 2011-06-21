package app.taskboard

import grails.plugins.springsecurity.Secured
import org.codehaus.groovy.grails.commons.ConfigurationHolder as grailsConfig

@Secured(['ROLE_USER'])
class BoardController {
	def springSecurityService
	def dashboardService

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
	
    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [boardInstanceList: Board.list(params), boardInstanceTotal: Board.count()]
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
			priorities:grailsConfig.config.taskboard.priorities 
		]
    }

    def save = {
		def boardInstance
		if (!params.id) {
			boardInstance = new Board()
		}
		else {
			boardInstance = Board.findByName(params.name)
		}
		
		if (!boardInstance) {
			flash.message = "Board instance can not be found for name ${params.name}."
			render(view: "create", model: [boardInstance: create()])
			return
		}		 

		bindData(boardInstance, params)	

		def principal = springSecurityService.principal
		def user = User.findByUsername(principal.username)
		assert user != null		
		boardInstance.users = [user]
		boardInstance.admins = [user]
		boardInstance.columns[params.workflowStart as int].workflowStartColumn = true

		if (boardInstance.validate() && boardInstance.save(flush:true)){	
			redirect(action: "show", id: boardInstance.id)			
		}
		else {
			render(view: "create", model: [boardInstance: boardInstance])
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
            return [boardInstance: boardInstance]
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
	
	
}
