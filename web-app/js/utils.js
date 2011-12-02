/**
 * Utility functions for general reuse
 */


//Globally holds the column where a sort action started - see start event of the sortable below
var startColumnId
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
		receive: moveTask,
		update: reorderTask,
		start: function(event, ui) {
			startColumnId = ui.item.parents('[id^="column_"]').attr('id');
		}
	});
}


/**
 * Callback for a connected sortable receiving a new Task object.
 * Updating the server side and taking case of updating the column counts. 
 *
 */	        	             
var moveTask = function(event, ui) {
	var fromColumnId = $(ui.sender).parents('[id^="column_"]').attr('id').split('_')[1];
	var toColumnId = $(this).parents('[id^="column_"]').attr('id').split('_')[1];		
	var toIndex = ui.item.index();	
	var taskId = $(ui.item).attr('id').split('_')[1];
	
	//Post onto controller "column" and action "updatetasks"
	$.ajax({
		type: 'POST',
		url: moveTaskUrl,
		data: {
			'fromColumn': fromColumnId, 
			'toColumn': toColumnId,
			'toIndex': toIndex,
			'taskid': taskId		
		}
	});				
}

/**
 * Submits a task reordering (within the same column) to the server
 * 
 */
var reorderTask = function(event, ui) {	
	//We only want to run this if it was a reordering in the same column.
	//Otherwise the moveTask function will take care.	
	var thisColumnId = ui.item.parents('[id^="column_"]').attr('id');
	if (thisColumnId == startColumnId) {			
		var position = ui.item.index();
		$.ajax({
			type: 'POST',
			url: reorderTaskUrl+'/'+ui.item.attr('id').split('_')[1],
			data: {
				'position': position
			}
		});
	}	
}
