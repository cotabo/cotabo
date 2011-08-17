package org.cotabo

class BoardFilters {
	def springSecurityService
	
    def filters = {
        boardShowView(controller:'board', action:'show') {
            before = {
                def board =  Board.get(params.id)							
				def user = User.findByUsername(springSecurityService.principal.username)
				if (!board.users.find{it == user}) {
					render(status:403, view:'notallowed', model:[boardInstance:board])
					return false					
				}
				return true;				
            }
        }
		boardEditView(controller:'board', action:'edit') {
			before = {
				def board = Board.get(params.id)
				def user = User.findByUsername(springSecurityService.principal.username)				
				if (!board.getUsers(RoleEnum.ADMIN).find{it == user}) {
					render(status:403, view:'notallowed', model:[boardInstance:board])
					return false
				}
			}
		}
    }
}
