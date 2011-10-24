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
    	$.atmosphere.request={transport:'websocket', dataType:'json'}
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
 * Atmosphere callback handling. For available message types:
 * refer: org.cotabo.MessageTypes
 ********************************************************/	

/**
 * Callback function dispatching based on message type.
 * Function to be registered for as atmosphere callback.
 * 
 * @param response JSON representation of the message.
 */
var atmosphereCallback = function(response) {
	if (response.status == 200 && response.state != 'connected' && response.state != 'closed') {
		try {
			var data = $.parseJSON(response.responseBody);
		}
		catch (e) {
			//alert('Error: '+e+'\nJSON: '+response.responseBody)
		}
		
		//console.log(data);
		
		if (data != null) {
			if(data.notification != null) {
				$.pnotify({
					pnotify_text: data.notification,					
					pnotify_history: true,
					pnotify_shadow: true,
					pnotify_delay: 20000
				});
			}
			switch(data.type) {
				case "all":
					reloadAllCallback(data);
					break;
				case "task":
					taskCallback(data);
					break;
				case "task_movement":
					taskMovementCallback(data);
					break;
				case "task_reordering":
					taskMovementCallback(data);
					break;
				case "task_creation":
					taskCreationCallback(data);
					break;	
				case "task_block":
					taskBlockCallback(data);
					break;
				case "task_update":
					taskUpdateCallback(data);
					break;
				case "chat_message":
					chatMessageCallback(data);
					break;
				default:
					break;
			}
		}
	}
} 



/**
 * Callback for task movements (across columns or within the same column).
 * 
 * Expecting task (id), toColumn (id), fromColumn (id) in the JSON.
 * Moves the task DOM element to the target position and does the animation of it.
 * 
 * @param data JSON representation of task movement tata
 * @returns
 */
var taskMovementCallback = function(data) {
	var taskDom = $('li#task_'+data.task);
	var toColumnDom = $('ul#column_'+data.toColumn);
	var fromColumnDom = $('ul#column_'+data.fromColumn);
	
	//Updating the assignee on task movements in any case
	if(data.type == 'task_movement') {
		//Update the assignee & the avatar
		$('td#task_'+data.task+'_assignee').html(data.assignee);		
		$('li#task_'+data.task+' > div > img.avatar').attr('src', avatarUrl + '/' + data.assignee);		
	}
	
	//Do nothing when task is already in target column
	//(meaning that this client triggered the message / callback)
	if ($(taskDom).parent().attr('id') == $(toColumnDom).attr('id') &&
			data.type == 'task_movement' ) {
		return;
	}
	else if(data.type == 'task_reordering') {
		//This is for checking whether it was a reorder triggered from this cliebt
		var correctOrder = true
		var collection = $(toColumnDom).children('li').toArray()
		for (var i = 0; i < collection.length; i++) {
			if ($(collection[i]).attr('id') != 'task_'+data.newTaskOrderIdList[i]) {
				correctOrder = false;
			}
		}
		if (correctOrder) {
			return;
		}		
	}
	
	var foundFlag = false;
	var successorDom = null;				
	//Find the task DOM that comes after the one moved in the newEntryIdList
	//so that we can use .before() to insert the task on the correct place.
	for (var i = 0; i < data.newTaskOrderIdList.length; i++) {			
		if (foundFlag == true) {						
			//We reached the successor of the moved task and select that piece of DOM
			successorDom = $('li#task_'+data.newTaskOrderIdList[i]);
			//Quit the loop when found
			break;
		}
		if (data.newTaskOrderIdList[i] == data.task) {
			foundFlag = true;
		}
	}	
	//TODO: check whether it is already moved - in this case
	//this is our own message and we don't need to do the DOM manupilation
	$(taskDom).draggable( "disable" )
	//Find the target for the animation
	var targetId
	if(successorDom != null) {
		targetId = $(successorDom).attr('id');
	}
	else {
		targetId = $(toColumnDom).children("li:last").attr('id');
	}
	if (targetId == null) {
		targetId = $(toColumnDom).attr('id');
	}	
	var effectOptions = { to: "#"+targetId, className: "ui-effects-transfer" };
	$(taskDom).effect('transfer', effectOptions, 1000, function() {
		if (successorDom != null) {
			$(taskDom).insertBefore($(successorDom));
		}
		//Assuming an empty column or a move to the bottom
		else {
			$(toColumnDom).append($(taskDom))
		}		
		$(taskDom).fadeIn(500);
		if (data.type == 'task_movement') {
			setElementCountOnColumn();
		}
	});			
	
	$(taskDom).draggable( "enable" )	
}


/**
 * Callback function for task_update events.
 * 
 * Updates the task on the board by removing & recreating the DOM element
 * on the correct position.
 * 
 * @param data JSON representing the task
 * @returns
 */
var taskUpdateCallback = function(data) {
	var id = data.id;
	var taskDom = $('li#task_'+data.id);	
	var nextDom = $(taskDom).next('li');
	var column = $(taskDom).parents('ul');	
	taskDom.remove();
	if (nextDom == null || nextDom == 'undefined' || $(nextDom).length == 0) {
		$(column).tplAppend(data, taskTpl);
	}
	else {
		$(nextDom).tplInsertBefore(data,taskTpl);
	}
}


var taskCallback = function(data) {
	var id = data.id;
	var taskDom = $('li#task_'+data.id);		
	//If the element exists
	if (taskDom.id != null && taskDom.id != 'undefined') {
		//Replace the existing tasks
		taskDom.replaceWith(data.rendered);
	}
	//element doesn't exist in task creation
	else {
		//Append the new element
		var newDom = $('.column:first > ul').append(data.rendered);
		//And run an effect
		$('#b_new_task').effect("transfer", {to:'li#task_'+data.id, className: "ui-effects-transfer"}, 1000);			
	}	
}

//Custom stack for chat messages - needs to be out of the function scope:
//see http://sourceforge.net/projects/pines/forums/forum/960539/topic/4495970
var stack_topleft = {"dir1": "down", "dir2": "right", "push": "top"};
/**
 * Callback for chat events (displaying them on the client).
 * Handles the chat message distributes by atmosphere.
 *  
 * @param data  JSON representing the chat message (notably chat_message:<string>)
 * @returns
 */
var chatMessageCallback = function(data) {		
	if (data.chat_message != null) {		
		$.pnotify({
			pnotify_title: data.chat_user,
			pnotify_text: data.chat_message,					
			pnotify_shadow: true,
			pnotify_history: true,			
			pnotify_notice_icon: 'ui-icon ui-icon-comment',
			pnotify_delay: 15000,
			pnotify_addclass: 'stack-topleft',			
			pnotify_stack: stack_topleft,
			pnotify_nonblock: false
		});
	}
}

var reloadAllCallback = function(data) {
	location.reload(true);
}