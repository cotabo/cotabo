package org.cotabo

import org.codehaus.groovy.grails.commons.ApplicationHolder

import groovy.util.slurpersupport.GPathResult

/**
 * Provides service methods for importing data into Cotabo
 * 
 * @author Robert Krombholz
 *
 */
class ImportService {

    static transactional = true

	def sessionFactory
	def springSecurityService
	
	
	/**
	 * Imports XML that was exported from the application.
	 * The structure is mainly defined through the 
	 * <a href="https://github.com/cotabo/cotabo-export-plugin">forked grails export plugin</a> 
	 *  - meaning by whatever is defined on the static 'exportables' property on each domain class.
	 * 
	 * <b>Note:</b> changes to the exportables property should also cause this method to change
	 * as it is currently extremely static.
	 * 
	 * 
	 * @param importXml exported XML
	 * @return
	 */
    def importBoards(importXml) {
		//TODO: make this dynamic
		def xml = new XmlSlurper().parseText(importXml)
		xml.board.each { xmlBoard ->			
			def board = new Board(
				name: xmlBoard.name.text(),
				description: xmlBoard.description.text()
			)
			board.save(flush:true)
			
			//Add all colors uniquely to the Board
			def uniqueColors = xmlBoard.columns.column*.tasks.task*.colors.taskColor*.collect{				
				[name: it.name.text(), color:it.color.text()]
			}.flatten().unique()
			log.debug "Unique Colors: $uniqueColors"		
			uniqueColors.each { board.addToColors(it) }
			board.save(flush:true)
			
			xmlBoard.columns.column.each { xmlColumn ->
				def column = new Column(
					name: xmlColumn.name.text(),
					description: xmlColumn.description.text(),
					limit: xmlColumn.limit.text(),
					workflowStartColumn: xmlColumn.workflowStartColumn.text(),
					workflowEndColumn: xmlColumn.workflowEndColumn.text()
				)
				board.addToColumns(column)	
				board.save(flush:true)			
				xmlColumn.tasks.task.each { xmlTask ->
					def task = new Task(
						name: xmlTask.name.text(),
						description: xmlTask.description.text(),
						details: xmlTask.details.text(),
						priority: xmlTask.priority.text(),
						color: xmlTask.color.text(),
						creator: User.findByUsername(xmlTask.creator.text()) ?: User.findByUsername(springSecurityService.principal.username),
						assignee: User.findByUsername(xmlTask.creator.text()),
						archived: xmlTask.archived.text(),						
						workflowStartDate: xmlTask.workflowStartDate.text() ? Date.parse("yyyy-MM-dd HH:mm:ss.SSS", xmlTask.workflowStartDate.text()) : null,
						workflowEndDate: xmlTask.workflowEndDate.text()? Date.parse("yyyy-MM-dd HH:mm:ss.SSS", xmlTask.workflowEndDate.text()) : null
					)
					column.addToTasks(task)					
					xmlTask.blocks.block.each { xmlBlock ->
						task.addToBlocks(
							dateCreated: xmlBlock.dateCreated.text() ? Date.parse("yyyy-MM-dd HH:mm:ss.SSS", xmlBlock.dateCreated.text()) : null,
							dateClosed: xmlBlock.dateClosed.text() ? Date.parse("yyyy-MM-dd HH:mm:ss.SSS", xmlBlock.dateClosed.text()) : null
						)
					}
					task.save(flush:true)
					//Map the TaskColors
					xmlTask.colors.taskColor.each { taskColor ->						
						def tmpColor = TaskColor.findByColorAndName(taskColor.color.text(),taskColor.name.text())						
						//Add it to the task
						task.addToColors(tmpColor)						
					}
				}
				//Saving afer all tasks for a column have been loaded
				column.save(flush:true)					
			}
			if(board.validate()) {
				board.save(flush:true)
				UserBoard.create(
					User.findByUsername(springSecurityService.principal.username),
					board,
					RoleEnum.ADMIN)
			}
			else {
				throw new TaskBoardException(board.errors.allErrors.join('\n'))
			}			
		}		
    }
}
