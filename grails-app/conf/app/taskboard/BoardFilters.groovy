package app.taskboard

class BoardFilters {
	def springSecurityService
	
    def filters = {
        boardView(controller:'board', action:'show') {
            before = {
                def board =  Board.get(params.id)							
				def user = User.findByUsername(springSecurityService.principal.username)
				if (!board.users.find{it == user} && !board.admins.find{it==user}) {
					render(view:'notallowed', model:[boardInstance:board])
					return false					
				}
				return true;				
            }
        }
    }
}
