/**
 * Utility functions for general reuse
 */


/**
 * Makes a single column a sortable that is connected with all other columns
 * @param column - a DOM object representing the column UL element
 */
var applySortable = function(column) {
	$(column).sortable({	 		
		//Connect the current sortable only with the next column
		//connectWith:'.column ul:gt('+index+'):first',
		connectWith:'.column ul',
		appendTo: 'body',
		containment:"#board",
		cursor:"move",
		distance:30,
		opacity:0.7,
		placeholder:'ui-effects-transfer',
		receive: moveTask		
	});
}


/**
 * Callback for a connected sortable receiving a new Task object.
 * Updating the server side and taking case of updating the column counts. 
 *
 */	        	             
var moveTask = function(event, ui) {
	var toColumnId = $(this).parents('[id^="column_"]').attr('id').split('_')[1];		
	var toIndex = ui.item.index();	
	var fromColumnId = $(ui.sender).parents('[id^="column_"]').attr('id').split('_')[1];
	var taskId = $(ui.item).attr('id').split('_')[1];
	
	//Post onto controller "column" and action "updatetasks"
	$.ajax({
		type: 'POST',
		url: moveTasksUrl,
		data: {
			'fromColumn': fromColumnId, 
			'toColumn': toColumnId,
			'toIndex': toIndex,
			'taskid': taskId		
		}
	});				
}

/**
 * Callback function for the returned html that represents the edit dialog.
 */
var appendUpdateDialogToDOM = function(data, textStatus, jqXHR) {
	//This also evaluates the contained script elements
	$('body').append(data);
}


/************** Reporting Stuff **************/ 



/**
 * Callback function for task_block events.
 * 
 * Marks the corresponding tasks in the gui as blocked / unblocked.
 * 
 * @param data JSON representing the task block data (only {task:<id>, block:<boolean>} 
 * @returns
 */
var taskBlockCallback = function(data) {
	var taskBlockDiv = $('li#task_'+data.task).find('span.block-box');	
	if((data.blocked == true) && $(taskBlockDiv).hasClass('not-blocked')) {
		$(taskBlockDiv).removeClass('not-blocked').addClass('blocked');
		$(taskBlockDiv).removeClass('ui-icon-unlocked').addClass('ui-icon-locked');
	}
	else if ((data.blocked == false) && $(taskBlockDiv).hasClass('blocked')){
		$(taskBlockDiv).removeClass('blocked').addClass('not-blocked');	
		$(taskBlockDiv).removeClass('ui-icon-locked').addClass('ui-icon-unlocked');
	}
}
