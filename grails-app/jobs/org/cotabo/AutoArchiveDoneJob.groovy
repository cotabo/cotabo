package org.cotabo

import org.codehaus.groovy.grails.commons.ConfigurationHolder as grailsConfig

class AutoArchiveDoneJob {
    def timeout = 1000l * 60l * 60l * 24l // execute job once a day

	def sessionFactory
	
    def execute() {
		// archive
        Task.getAll().each{task ->
			if(task.column.workflowEndColumn && new Date() - task.lastUpdated > grailsConfig.taskboard.default.autoarchive){
				task.archived = true;
				task.save(flush:true); //TODO: bulk update/flush
			}
        }
    }
}
