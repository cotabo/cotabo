package org.cotabo

import grails.test.*

import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory
import org.atmosphere.cpr.DefaultBroadcaster
import org.atmosphere.cpr.AtmosphereResource
import org.atmosphere.cpr.AtmosphereResourceEvent
import org.codehaus.groovy.grails.plugins.testing.GrailsMockHttpSession


class BoardUpdateServiceTests extends GrailsUnitTestCase {
	
	private atmosphereResourceControl
	private atmosphereResourceEventControl 
	
	
    protected void setUp() {
        super.setUp()
		//We create loose mocks as we don't care about the order
		//(would get too complicated)
		atmosphereResourceControl = mockFor(AtmosphereResource, true)
		atmosphereResourceEventControl = mockFor(AtmosphereResourceEvent, true)
		def broadcasterFactoryControl = mockFor(BroadcasterFactory, true)
		def broadcasterControl  = mockFor(Broadcaster, true)
				
		
		//Mocking atmosphereResource
		def theSession = new GrailsMockHttpSession()
		atmosphereResourceControl.demand.getRequest(4..4) {
			return new Expando(
				session: theSession,
				getParameter: { String param -> return 1 }
			)
		}
		def resp = new Expando(writer: new StringWriter(), setContentType: {}, setCharacterEncoding: {})
		atmosphereResourceControl.demand.getResponse(4..4) {
			return resp
		}		
		//This gets called during compairson of mocked objects
		atmosphereResourceControl.demand.toString(2..2) {
			return '1'
		}
		
		//We just want to make sure that it gets called 1 time
		atmosphereResourceControl.demand.suspend(1..1) {}
		
		def theBroadcaster
		atmosphereResourceControl.demand.getBroadcaster(2..2) {
			return theBroadcaster
		}
		atmosphereResourceControl.demand.setBroadcaster(1..1) { bc ->
			theBroadcaster = bc
		}
		
		//Mocking AtmosphereResourceEvent
		def theResource
		atmosphereResourceEventControl.demand.getResource(4..4) {
			return theResource
		}
		atmosphereResourceEventControl.demand.setResource(1..1) { asource ->
			theResource = asource
		}
		atmosphereResourceEventControl.demand.isResuming(1..1) { return false }
		atmosphereResourceEventControl.demand.isResumedOnTimeout(1..1) { return false }
		def message
		atmosphereResourceEventControl.demand.setMessage(1..1) { m -> message = m }
		atmosphereResourceEventControl.demand.getMessage(2..2) { return message }
		
				
		//Mocking Broadcaster
		def resources = []
		broadcasterControl.demand.addAtmosphereResource(1..1) { bc ->
			resources << bc
			bc 			
		}
		broadcasterControl.demand.getResources(1..1) {
			return resources
		}
				
		//Mocking BroadcasterFactory & sub-object that we use
		BroadcasterFactory.metaClass.static.getDefault = {
			def exp = new Expando(
				lookup: {Class clazz, String channel, boolean createIfNull ->								
					def tmpBroadcasters = delegate.broadcasters
					def bc = delegate.broadcasters[channel]					
					if (!bc) {
						bc = broadcasterControl.createMock()
						delegate.broadcasters[channel] = bc
					}									
					return bc						
						
				},
				broadcasters: [:]
			)
			return exp
		}
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testOnRequestClosure() {
		def atmosphereResource = atmosphereResourceControl.createMock()
		def boardUpdateService = new BoardUpdateService()
		boardUpdateService.onRequest(atmosphereResource)		
		def expectedBC = BroadcasterFactory.default.lookup(
				 DefaultBroadcaster.class,
				 '/atmosphere/boardupdate/1',
				 true
		)		
		//Check whether the expected broadcaster is set on the resource
		assertEquals expectedBC, atmosphereResource.getBroadcaster()
		//Check whether the resource is added to the broadcaster
		assertEquals atmosphereResource.broadcaster.getResources(), [atmosphereResource]		
		//Check whether the resource was put in the session
		assertEquals atmosphereResource, atmosphereResource.request.session.getAttribute('boardBroadacster')				
    }
	
	void testOnStateChange() {
		def atmosphereResource = atmosphereResourceControl.createMock()
		def atmosphereResourceEvent = atmosphereResourceEventControl.createMock()
		def boardUpdateService = new BoardUpdateService()
		//Prepare the object
		atmosphereResourceEvent.message = '{"test":"test"}'
		atmosphereResourceEvent.resource = atmosphereResource
		//The expectec message to be written to the responseWriter
		def expectedMessage = "<script>parent.callback('{\"test\":\"test\"}');</script>"
		//Call the service closure
		boardUpdateService.onStateChange(atmosphereResourceEvent)
		assertEquals expectedMessage, atmosphereResourceEvent.resource.response.writer.toString() 
	}
}
