/**
 * Utility functions for general reuse
 */


/**
 * resets the state of a dialog (removing errors etc.)
 * 
 * @param dialogSelector: The selector for the dialog container element.
 */
var resetDialogErrors = function(dialogSelector) {
	$(dialogSelector+' :input').removeClass('error');
	$(dialogSelector+' div.ui-state-error').remove();
}

/**
 * Update all columns task counts.
 * Setting this on the window as we need it in a different script block (task creation)
 */   
var setElementCountOnColumn = function() {        		
	$(".column").each(function(index){	    			
		var size = $(this).children("ul").children("li").size();
		if (size == null) {
			size = 0 ;
		}
		//Set the current number of elements to the element count for each column
		var pValueDom = $(this).children("span").children("span").children("p.value");
		var pLimitDom = $(this).children("span").children("span").children("p.limit");
		pValueDom.text(size);
		if (pValueDom.text() == pLimitDom.text() || parseInt(pValueDom.text()) > parseInt(pLimitDom.text())) {
			$(pValueDom).addClass('red-font');
		}
		else {
			if ($(pValueDom.hasClass('red-font'))) {
				pValueDom.removeClass('red-font');
			}
		}
	});	
}  

/**
 * Checks for errors in a JSON response of a validated Grails domain object.
 * It prints error messages and applies the .error class to all invalid input elements 
 * (assuming they have the same name as the class property).
 * 
 * @params responseData: the response data from the server as evaluated JSON object.
 * @params formContainerSelector: the selector for the container in which the form sits in (e.g. a div representing a jQuery dialog).
 * 
 * @returns true if the request was successfull or false when errors appeared during server side validation.
 */
var checkForSuccess = function(responseData, formContainerSelector) {	
	if(responseData.errors != null) {
		$(formContainerSelector).prepend('<div class="ui-state-error ui-corner-all"></div>');
		$(responseData.errors).each(function(index, element) {
			$(formContainerSelector + ' > .ui-state-error').append('<p><span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-alert"></span>' + element.message + '</p>');
			$(formContainerSelector + ' :input[name="'+element.field+'"]').addClass('error');
		});
		return false;
	}
	else {
		return true;
	}
}


/**
 * Representing a skeleton task object that can be rendered 
 * with the flydom plugin for jQuery (http://dohpaz.com/flydom/).
 * The main purpose is to use it with the tplAppend / tplPrepend function.
 * 
 * This is basically the same as the tag tb:task renders but as
 * page rendering mechanisms can not be used with AJAX request we need to maintain
 * the task structure here and inside the BoardTagLib. 
 * 
 */
var taskTpl = function () {
	var theId = this.id
	return [ 
		"li", {class:'ui-widget', id:'task_'+this.id}, [
			"div", {class:'task-header ui-state-default'}, [
				"div", {class:'head_color', style:'background:'+this.color}, ,
				"div", {class:'head_name'}, this.name,
				"div", {class:'block-box not-blocked'}, ,
				"span", {class:'ui-icon ui-icon ui-icon-carat-1-n'}, 
			],
			"div", {class:'task-content ui-widget-content', style:'display:block;'}, [
				"table", {}, [
					"tbody", {}, [
						"tr", {}, [
							"td", {}, [
								"b", {}, 'Id:'									
							],
							"td", {}, ''+this.id//this.id							
						],					              
						"tr", {}, [
							"td", {}, [
								"b", {}, 'Description'									
							],
							"td", {}, this.description								
						],
						"tr", {}, [
							"td", {}, [
								"b", {}, 'Priority'									
							],
							"td", {}, this.priority								
						],
						"tr", {}, [
							"td", {}, [
								"b", {}, 'Assignee'									
							],
							"td", {}, this.assignee		
						]
					]
				]
			]    		
		]
	];
}
/************** Atmosphere Stuff *************/ 

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
    	$.atmosphere.request={transport:'websocket', fallbackTransport:'streaming', dataType:'json'}
	);    
}

/**
 * Function to be registered for atmosphere taskMovement events.
 * This moves tasks (or reorders them) based on the response date 
 * which was generated out of the TaskMovementMessage.
 * @param response JSON representation of TaskMovementMessage
 */
var atmosphereCallback = function(response) {
	if (response.status == 200 && response.state != 'connected' && response.state != 'closed') {
		try {
			var data = $.parseJSON(response.responseBody);
		}
		catch (e) {
			//alert('Error: '+e+'\nJSON: '+response.responseBody)
		}		
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
		$('td#task_'+data.task+'_assignee').html(data.assignee);
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
 * Doing the reordering + animation of sorta inside a column.
 * 
 * @param data JSON representation of task movement tata 
 * @returns
 */
var taskReorderingCallback = function(data) {
	var taskDom = $('li#task_'+data.task);
	var toColumnDom = $('ul#column_'+data.toColumn);
	var fromColumnDom = $('ul#column_'+data.fromColumn);	
}

/**
 * Expecting all domain properties of a Task object in represented as JSON.
 * Doing the task creation animation + inserting the actual dom element.
 * 
 * @param data JSON representation of task tata
 * @returns
 */
var taskCreationCallback = function(data) {
	//helper div for the animation see board/_menu.gsp	
	var helper = $("#new_task_helper");
	var targetId = $("div.column:first > ul > li:last").attr('id');
	//In case that the column is empty	
	if (targetId == null) {		
		targetId = $("div.column:first > ul").attr('id');
	}	
	//alert(targetdId);
	var effectOptions = { to: "#"+targetId, className: "ui-effects-transfer" };
	$('#createTaskForm').dialog('close');
	
	$(helper).css('display','block');	
	$(helper).effect('transfer', effectOptions, 1000,function() {
		var createdDom = $("div.column:first").children("ul").tplAppend(data, taskTpl);
		setElementCountOnColumn();	
	});
	$(helper).css('display','none');		
}

/**
 * Markes the corresponding tasks in the gui as blocked / unblocked.
 * 
 * @param data JSON representing the task block data (only {task:<id>, block:<boolean>} 
 * @returns
 */
var taskBlockCallback = function(data) {
	var taskBlockDiv = $('li#task_'+data.task).find('div.block-box');	
	if((data.blocked == true) && $(taskBlockDiv).hasClass('not-blocked')) {
		$(taskBlockDiv).removeClass('not-blocked').addClass('blocked');
	}
	else if ((data.blocked == false) && $(taskBlockDiv).hasClass('blocked')){
		$(taskBlockDiv).removeClass('blocked').addClass('not-blocked');		
	}
}
//Custom stack for chat messages - needs to be out of the function scope:
//see http://sourceforge.net/projects/pines/forums/forum/960539/topic/4495970
var stack_topleft = {"dir1": "down", "dir2": "right", "push": "top"};
/**
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


/************** Reporting Stuff **************/ 
/**
 * Gets the data for the Colulative Flow Diagram for a single column 
 * and splits the returning CSV data <timestamp,value>
 * into a series of Arrays so that it can be used directly by Flot
 * to be inserted as a data series.
 *
 * @param columnId - the column ID that you want to get data for
 * @return Array of arrays for each data point
 */
var getCsvCDFDataForColumn = function (columnId) {
	return syncGetOnCSVReturningController(columnId, 'getCdfDataForColumn');
}

/**
 * Get the data for displaying the lead-time and receives the CSV data
 * <timestamp,value> into a series of Arrays so that is can be used directly
 * by jQuery Flot as time-series data.
 * 
 * @param boardId - the board the data should be got for.
 * @return Array of arrays for each data point.
 */
var getLeadTimeData = function(boardId) {
	return syncGetOnCSVReturningController(boardId, 'getLeadTimeData');
}

/**
 * Get the data for displaying a courve of how many tasks are inside
 * the bonderies of the workflow.
 *
 * @param boardId - the board the data should be got for.
 * @return Array of arrays for each data point.
 */
var getWorkflowTaskAmountData = function(boardId) {
	return syncGetOnCSVReturningController(boardId, 'getWorkflowTaskAmountData');
}
/**
 * Helper function for getting CSV data where we need to
 * raise synchronours get requests for.
 * 
 * @param objectId - the id of the object that the action needs (e.g. board id or column id)
 * @param action - the action on the BoardController that should be requested
 * @return an array of arrays (one for each returned data point / CSV line)
 */
var syncGetOnCSVReturningController = function(objectId, action) {
	//Setting asynchronous ajax to false because we want to load the data
	//before executing the diagram-generation code.
	$.ajaxSetup({async:false});
	var resultData;
	$.get('../'+action+'/'+objectId, function(data) {
		resultData = $.csv(',', 0)(data);
	});
	$.ajaxSetup({async:true});
	return resultData;
}