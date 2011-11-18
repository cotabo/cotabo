/**
 * Everything related to atmosphere (subscription & callbacks).
 */

/********************************************************
 * Section for Atmosphere subscription.
 * refer: services/BoardUpdateService.onRequest
 ********************************************************/	

/**
 * Subscribes the an atmosphere channel for the given boardId.
 * @param boardId The id of the current board context
 */
var subscribeChannel = function(channelString, callback) {
	//Closing all active requests if there are any
	$.atmosphere.close(); 
    $.atmosphere.subscribe(channelString,
    	callback,
    	//Use WebSockets as the first choice but use streaming if
    	//either the server or the client doesn't support WebSockets.
    	$.atmosphere.request={transport:'websocket'}
	);    
}
//onload
jQuery(function(){
    /**
     * Subscribing the atmosphere channel for this board and register the atmosphereCallback
     */
     subscribeChannel(atmosphereSubscriptionUrl, atmosphereCallback);  
});


/********************************************************
 * Atmosphere callback handling. 
 ********************************************************/	

/**
 * Callback function dispatching based on message type.
 * Function to be registered for as atmosphere callback.
 * 
 * @param response HTML response
 */
var atmosphereCallback = function(response) {			
	if (response.status == 200 && response.state != 'connected' && response.state != 'closed') {						
		if (response.responseBody != null && response.respondeBody != 'keepalive' 
			&& response.responseBody != 'resuming') {
			rerenderCallback(response.responseBody);		
		}
	}
} 

/**
 * Atmosphere callback.
 * Currently data holds the URL that is used to request the DOM for an object from the Server.
 */
var rerenderCallback = function(broadcast) {	
	//Potentially we got a list of URLs
	var urls = broadcast.split('\n')
	for (var i = 0; i < urls.length; i++) {
		//should start with a / as it's a context specific URL
		if (urls[i].indexOf('/') != -1) {				
			console.log(urls[i]);
			$.get(urls[i], function(data, textStatus){				
				var rerenderedDom = $(data);
				var id = rerenderedDom.first().attr('id');
				var oldObjectDom = $('#'+id);
				if (oldObjectDom.attr('id') != null && oldObjectDom.attr('id') != 'undefined') {				
					//Replace the existing DOM
					oldObjectDom.replaceWith(rerenderedDom);
					//When it was a column - reapply the sotable
					if (id.indexOf('column_') == 0) {
						//Need to use the UL below the container for this
						applySortable($('#'+id + ' > ul'));
					}
				}
				//Happens on task creations
				else {
					//If the ID starts with task_
					if (id.indexOf('task_') == 0) {
						$('.column:first > ul').append(rerenderedDom);						
					}
				}
				rerenderedDom.effect('highlight', 500);
			});
		}
	}
}

var reloadAllCallback = function(data) {
	location.reload(true);
}
