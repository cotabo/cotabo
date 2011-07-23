package org.cotabo

import java.util.concurrent.TimeUnit;

import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory
import org.atmosphere.cpr.DefaultBroadcaster

import grails.converters.JSON


/**
 * This service contains everything related to atmosphere based baord updates.
 * It also acts as the formal AtmosphereHandler implementation (even if it doesn't extend it).
 * 
 * See https://bitbucket.org/bgoetzmann/grails-atmosphere-plugin/wiki/Home for more information.
 * 
 * @author Robert Krombholz
 *
 */
class BoardUpdateService {
	
    static transactional = true

	//This actually makes this service an AtmosphereHandler
	static atmosphere = [mapping: '/atmosphere/boardupdate']
	
	//Holds the channel strings for all broadcasters that
	//already have a scheduled broadcast
	def scheduledChannels = []
	/**
	 * See http://atmosphere.java.net/nonav/apidocs/org/atmosphere/cpr/AtmosphereHandler.html#onRequest(org.atmosphere.cpr.AtmosphereResource)
	 * 
	 * We're hacking the Grails Atmosphere plugin a bit here.
	 * Normally it is intended to only have 1 atmosphere channel
	 * but we may create a new one for each boardId that arrives
	 * and put this into the users session.
	 *
	 * If we find a broadcaster in the session it means that the user switched the channel
	 * (or the board context) and we need to remove him from the existing broadcaster and put him
	 * onto a new one depending on the boardId request parameter.
	 *
	 * This closure gets called whenever someone is sending a request too
	 *   /atmosphere/boardupdate
	 * The boardId should be sent with as a request parameter.
	 * Based on this - we will put the user in a channel called /atmosphere/boardupdate/&lt;boardId&gt;
	 */
	 def onRequest = { event ->		 
		 //We create try to get (or create a new one if not yet there) a broadcaster
		 //For the boardId that was sent as a request parameter
		 def channel = '/atmosphere/boardupdate/'+event.request.getParameter('boardId')
		 def boardSpecificBroadcaster =
			 BroadcasterFactory.default.lookup(
				 DefaultBroadcaster.class,
				 channel,
				 true
			 )
		 def sessRes = event.request.session.getAttribute('boardBroadacster')		 
		 if(sessRes) {
			 //We need to remove the AtmosphereResource from the session
			 //from its broadcaster.
			 sessRes.broadcaster.removeAtmosphereResource(sessRes)
			 sessRes.broadcaster = null
		 }
		 //We set the newly retrieved broadcaste for this user
		 event.setBroadcaster(boardSpecificBroadcaster)		 
		 //We subscribe the user to the broadcaster
		 boardSpecificBroadcaster.addAtmosphereResource(event)
		 
		 //We put this AtmosphereResource into the users session
		 //So that it can be found on the next request to the controller
		 event.request.session.setAttribute('boardBroadacster', event)
		 
		 //Setting content type
		 event.response.setContentType('text/html;charset=UTF-8')
		 //Setting proper cache headers
		 event.response.addHeader("Cache-Control", "no-store")
		 event.response.addHeader("Cache-Control", "no-cache")
		 event.response.addHeader("Cache-Control", "must-revalidate");
		 event.response.addHeader("Pragma", "no-cache");
		 //For FireFox (see http://www-archive.mozilla.org/projects/netlib/http/http-caching-faq.html)
		 event.response.addHeader("Expires", "0")
		 //Suspending the request - waiting for events
		 event.suspend()
		 if (!scheduledChannels.find{it == channel}) {
			 boardSpecificBroadcaster.scheduleFixedBroadcast '{"type":"keepalive"}', 30, TimeUnit.SECONDS
			 scheduledChannels << channel			 
		 }

	}

	/**
	 * See http://atmosphere.java.net/nonav/apidocs/org/atmosphere/cpr/AtmosphereHandler.html#onStateChange(org.atmosphere.cpr.AtmosphereResourceEvent)
	 * 
	 * This reflects the receival of a message.
	 * We don't do much with it - we just pass is to the client in as we receive it.
	 * The message content can be trusted as we're not having an user input here.
	 */
	def onStateChange = { event ->				
		if (!event.message) return
		//Removing this event from the broadcatster?
		//Display a message?
		if (event.isCancelled()) return
		if (event.isResuming() || event.isResumedOnTimeout()) {
			event.resource.response.writer.with {
				write '{"type":"resuming"}'
				flush()
			}
		} else {			
			event.resource.response.setContentType('text/javascript;charset=UTF-8')
			event.resource.response.setCharacterEncoding('UTF-8')
			event.resource.response.writer.with {				
				write "<script>parent.callback('${event.message}');</script>"
				flush()
			}
		}
	}
	
	/**
	 * Asynchronously broadcasting a the given message as as JSON string to the given broadcaster.
	 * 
	 * @param broadcaster The Atmosphere Broadcaster object that the message should be distributed too.
	 * @param message The message contianing information about the movement.
	 */
    private void broadcastMessageAsJSON(def message, Broadcaster broadcaster) {			
		broadcaster.broadcast(message as JSON)
    }
	
	/**
	 * Distributes the given message to the users registered broadcaster.
	 *
	 * @param broadcaster The atmosphere broadcaster
	 * @param message whatever message should be sent over atmosphere
     * @param the type-string that will be used in client code
     * @param notification A notification that can be used on the client to display a message
	 */
	public void broadcastMessage(Broadcaster broadcaster, def message, String type, def notification = null) {	   
	   //We just do nothing if there is no broadcaster int he session.
	   if (broadcaster) {
		   message.type = type
		   message.notification = notification
		   broadcastMessageAsJSON(message, broadcaster)
	   }
	}
}
