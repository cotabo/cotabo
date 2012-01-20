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
    String render(Rerenderable obj, def rerenderAction = null) {
		if (obj) {
			def controllerContext = GrailsNameUtils.getShortName(obj.class).toLowerCase()
			def result = g.createLink(controller: controllerContext, action: (rerenderAction ?: obj.rerenderAction), id:obj.id)
			return result
		}
		else {
			return ''
		}
    }
}
