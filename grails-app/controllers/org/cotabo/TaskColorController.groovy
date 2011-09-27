package org.cotabo

class TaskColorController {
	
	static allowedMethods = [save: "POST"]

    def index = { }
	
	def save = {
		def taskColor = new TaskColor()
		
		bindData(taskColor, params)
		
		def board = Board.get(params.board)
		
		board.addToColors(taskColor)
		
		if (!taskColor.hasErrors()){
			board.save(flush:true)
			render ''
		}
	}
}
