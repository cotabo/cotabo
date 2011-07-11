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
	return [ 
		"li", {class:'ui-widget', id:'task_'+this.id}, [
			"div", {class:'task-header ui-state-default'}, [
				"div", {}, this.name,
				"span", {class:'ui-icon ui-icon ui-icon-arrowthickstop-1-n'}
			],
			"div", {class:'task-content ui-widget-content', style:'background:'+this.color}, [
				"table", {}, [
					"tbody", {}, [
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
    	$.atmosphere.request={transport:'websocket', fallbackTransport:'streaming'}
	);    
}

/**
 * Function to be registered for atmosphere taskMovement events.
 * This moves tasks (or reorders them) based on the response date 
 * which was generated out of the TaskMovementMessage.
 * @param response JSON representation of TaskMovementMessage
 */
var taskMovementCallback = function(response) {	
	if (response.status == 200 && response.state != 'connected' && response.state != 'closed') {
		
		var data = $.parseJSON(response.responseBody);		
		var taskDom = $('li#task_'+data.task);
		var toColumnDom = $('ul#column_'+data.toColumn);
		var fromColumnDom = $('ul#column_'+data.fromColumn);		
						
		//Do nothing when task is already in target column
		//(meaning that this client triggered the message / callback)
		if ($(taskDom).parent().attr('id') == $(toColumnDom).attr('id')) {
			return;
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
		var targetPosition = $(successorDom).position();
		$(taskDom).addClass('ui-state-active')
		$(taskDom).fadeOut(500, function() {
			$(taskDom).removeClass('ui-state-active')
			if (successorDom != null) {
				$(taskDom).insertBefore($(successorDom));
			}
			//Assuming an empty column or a move to the bottom
			else {
				$(toColumnDom).append($(taskDom))
			}
			$(taskDom).fadeIn(500);
		});			
		
		/*
		$(taskDom).animate({
			left: targetPosition.left,
			top: targetPosition.top				
		}, 1000, function() {
			$(taskDom).insertBefore($(successorDom));
			$(taskDom).css('position','static')
		})
		*/
		$(taskDom).draggable( "enable" )
		

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

