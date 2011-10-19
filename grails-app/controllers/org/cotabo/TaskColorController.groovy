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
			redirect(controller:'board', action:'show', id:board.id)
		}
	}
	
	def delete = {
		def taskColor = TaskColor.get(params.id)
		def parentBoard
		Task.findAll().each {task -> if (taskColor in task.colors) task.removeFromColors(taskColor)}
		Board.findAll().each {board -> if (taskColor in board.colors) board.removeFromColors(taskColor); parentBoard=board}
		taskColor.delete(flush:true)
		redirect(controller:'board', action:'show', id:parentBoard.id)
	}
}
