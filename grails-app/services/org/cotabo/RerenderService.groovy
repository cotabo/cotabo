package org.cotabo

import org.springframework.beans.factory.InitializingBean
import grails.util.GrailsNameUtils

class RerenderService implements InitializingBean {
	
    static transactional = true
	
	def gspTagLibraryLookup
	def g

	/**
	 * initializing the taglib
	 */
	public void afterPropertiesSet() {
		g = gspTagLibraryLookup.lookupNamespaceDispatcher("g")
		assert g
	}
	
	/**
	 * Returns the rendered HTML output for a Renderable object.
	 * By that - the object needs to have a _show gsp template in its context.
	 * 
	 * @param obj the object that should be rendered
	 * @return a rendered GSP template string
	 */
    String render(Rerenderable obj) {
		if (obj) {
			def templateContext = GrailsNameUtils.getShortName(obj.class).toLowerCase()
			//preparing the model (e.g. [taskInstance: obj]
			def modelKey = "${templateContext}Instance"
			def model = [:]
			model[modelKey] = obj			
			return g.render(
				template:"/${templateContext}/show".toString(), 
				model: model
			)
		}
		else {
			return ''
		}
    }
}
