package app.taskboard

import grails.plugins.springsecurity.Secured

@Secured(['ROLE_USER'])
class BoardController {
	def springSecurityService

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
			columnList << new Column(name:'To Do', description:'ToDo backlog', limit: 8)
			columnList << new Column(name:'In Progress', description:'Things that are currently in Progress', limit: 4)
			columnList << new Column(name:'Done!', description:'Things that have already been done')
			boardInstance.columns = columnList
		}
        return [boardInstance: boardInstance, colorInstances: Color.list(max:20)]
    }

    def save = {
		def boardInstance
		if (!params.id) {
			boardInstance = new Board()
		}
		else {
			boardInstance = Board.findByName(params.name)
		}		 
				 	
		bindData boardInstance, params	
		
		def principal = springSecurityService.principal
		def user = User.findByUsername(principal.username)
		assert user != null		
		boardInstance.users = [user]
		boardInstance.admins = [user]		

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
            [boardInstance: boardInstance]
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
}
