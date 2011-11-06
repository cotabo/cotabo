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
     * Subscribing the atmosphere channel for this board and register
     * the atmosphereCallback - see utils.js
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
		//console.log(response.responseBody);		
		if (response.responseBody != null) {
			rerenderCallback(response.responseBody);		
		}
	}
} 


var rerenderCallback = function(data) {		
	//TODO: ignore the keepalive 
	if (data != 'keepalive' && data != 'resuming') {
		//get the DOM of e.g. id=task_1 or id=column_2
		var rerenderdDom = $(data);
		var id = rerenderdDom.first().attr('id');
		var oldObjectDom = $('#'+id);		
		if (oldObjectDom.attr('id') != null && oldObjectDom.attr('id') != 'undefined') {
			oldObjectDom.replaceWith(rerenderdDom);
			//We it was a column - reapply the sotable
			if (id.indexOf('column_') == 0) {
				//Need to use the UL below the container for this
				applySortable($('#'+id + ' > ul');
			}		
			rerenderdDom.effect('highlight', 500);
		}
	}
}

var reloadAllCallback = function(data) {
	location.reload(true);
}