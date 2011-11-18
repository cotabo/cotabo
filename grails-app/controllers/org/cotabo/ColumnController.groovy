package org.cotabo

class ColumnController {
	
	def showDom = {
		def columnInstance = Column.get(params.id)
		if (columnInstance) {
			render(template: 'show', model:[columnInstance:columnInstance])
		}
	}
}
