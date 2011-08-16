/**
 * Holding all (mostly jQuery-ui) objects and their events that are 
 * build up onload.
 */
jQuery(function(){
	 /********************************************************
	 * Section for the keyboard navigation.
	 ********************************************************/
	 $(document).ready(function(){
       $("body").keypress(function(e)
       {
       	 	var shortcutsDisabled = (e.srcElement instanceof HTMLInputElement);
       	 	var code = (e.keyCode ? e.keyCode : e.which);
           if(!shortcutsDisabled) {
						 if (code == 110) /* 'n' */{
							 $('#createTaskForm').dialog('open');
						 }
						 if (code == 115) /* 's' */ {
							 $('#chat_dialog').dialog('open');
						 }
						 if (code == 101) /* 'e' */ {
							 fn_expand.call();
						 }
						 if (code == 99) /* 'c' */ {
							 fn_collapse.call();
						 }
						 e.preventDefault();
           }
       });
     });
		
	/********************************************************
	 * Section for the Board menu and everything that belongs to it.
	 * refer: board/_menu template
	 ********************************************************/	
	/**
	 * Button definitions
	 */
	$('#b_new_task').button({
		icons: {
			secondary: 'ui-icon-document'
		}		
	}).click(function() {
		$('#createTaskForm').dialog('open');
		return true;
	});
	
	var fn_collapse = function() {
		//console.time('native');		
		var matched = $('#board > div.column > ul > li > div.task-header > span.ui-icon-carat-1-n')		 		 	
		for (var i=0;i< matched.size();i++)	{
			$(matched[i]).click();		
		}
		//console.timeEnd('native'); 
		return true;
	};
	$('#b_collapse').button({
		icons: {
			secondary: 'ui-icon-carat-1-n'
		}
	}).click(fn_collapse);

	var fn_expand = function() {	
		console.time('native');	
		var matched = $('#board > div.column > ul > li > div.task-header > span.ui-icon-carat-1-s')	
		for (var i=0;i< matched.size();i++)	{
			$(matched[i]).click();
		}
		console.timeEnd('native'); 
		return true;
	};
	$('#b_expand').button({
		icons: {
			secondary: 'ui-icon-carat-1-s'
		}
	}).click(fn_expand);
	
	$('#b_chat').button({
	   icons: {
	       secondary:'ui-icon-comment'
	   }
	}).click(function() {
	   $('#chat_dialog').dialog('open');
	}).parent().buttonset();

	
	/**
	 * Posting the chat message to the server.
	 */
	var postChatMessage = function() {
	    var message = $('#chat_form').serialize();
	    $.ajax({
	         type: 'POST',
	         url: chatUrl,
	         data: message
	    });               
	    $('#chat_dialog').find('input').val('');
	    $('#chat_dialog').dialog('close');
	    $("input").blur();
	    return false;
	}
	//Binding the submit event of the chat form
	$('#chat_dialog').find('form').submit(postChatMessage);
	//Defining the Chat dialog
	$('#chat_dialog').dialog({
	   autoOpen:false,
	   height: 150,
	   width: 500,
	   modal: true,
	   buttons: {
	       "send": postChatMessage,
	       "cancel": function() {
	           $('#chat_dialog').find('input').val('');
	           $('#chat_dialog').dialog('close');
	       }
	   },
	   close: function(){
	   	 $("input").blur();
	   }
	});	
	
	
	/********************************************************
	 * Section for Task - DOM related events
	 * refer: board/show view
	 * refer: taglib/BoardTagLib
	 ********************************************************/
	
	/**
	 * Handler for expanding / collapsing tasks on click of the exapnd icon.	 
	 */
	var handleClickHeader = function(event) {
		//console.time('native'); 				
		var north = $(this).hasClass('ui-icon-carat-1-n');
		if (north) {
			$(this).removeClass('ui-icon-carat-1-n')
				.addClass('ui-icon-carat-1-s')
				.parent('div').next('div').css('display', 'none')					
		}
		else {
			$(this).removeClass('ui-icon-carat-1-s')
				.addClass('ui-icon-carat-1-n')
				.parent('div').next('div').css('display', 'block');										
		}		
		//console.timeEnd('native'); 
		return false;
	}	  
	
	//Apply the click handle to all expand/collapse icons
	$('.task-header .ui-icon').live('click', handleClickHeader);
	
	/**
	 * Handles a click on the block icon - updating the task on the server-side and
	 * by that distributing a task_block message type to all subscribed clients.
	 * 
	 */		 			
	$('.block-box').live('click', function() {
		var wasBlocked = $(this).hasClass('blocked');                
		var taskId = $(this).parents('li').attr('id');                
		$.ajax({        
		  type: 'POST',
		  url: updateTaskUrl+'/'+taskId.split('_')[1],
		  data: {wasBlocked:wasBlocked}       
	    }); 
	});
	
    /**
     * For Task updates - opens on double click of task content.
     */         
	$('div.task-content').live('dblclick', function() {
	     var id = $(this).closest('li').attr('id').split('_')[1];
	     $.ajax({
	         type: 'GET',
	         url: editTaskUrl+'/'+id,
	         dataType: 'html',
	         success: appendUpdateDialogToDOM
	     });			                     			     			     			     			 
	});	
	
	/**
	 * Mouse over/out for task header - adding/removing ui-state-hover
	 */
	var header = $('.task-header')
	header.live('mouseover', function() {
		$(this).addClass('ui-state-hover');
	});
	header.live('mouseout', function() {
		$(this).removeClass('ui-state-hover');
	});
	
	/********************************************************
	 * Section for the Board itself (connected sortable columns etc.)
	 * refer: board/show view
	 * refer: taglib/BoardTagLib
	 ********************************************************/	
	/**
	 * Callback for a connected sortable receiving a new Task object.
	 * Updating the server side and taking case of updating the column counts. 
	 *
	 */	        	             
	var updateConnectedColumn = function(event, ui) {
		var toColumnId = $(this).attr('id').split('_')[1];
		var taskId = $(ui.item).attr('id').split('_')[1]; 
		
			var fromColumnId = $(ui.sender).attr('id').split('_')[1];				
									
		//Post onto controller "column" and action "updatetasks"
		$.ajax({
			type: 'POST',
			url: moveTasksUrl,
			data: {
				'fromColumn': fromColumnId, 
				'toColumn': toColumnId,
				'taskid': taskId,
				'order': $(this).sortable("toArray")
			},
			success: function(data) {
				//Update the task counts on each column
				setElementCountOnColumn();
				//If an element is moved to the last column
				if (toColumnId == $(".column:last > ul").attr('id').split('_')[1]) {
					//and it is ellapsed			
					if ($(ui.item).children('div:first').children('span').hasClass('ui-icon-carat-1-n')) {
						//Collapse it
						$(ui.item).children('div:first').children('span').click()
					}
				}
			}
		});				
	}

	
	/**
	 * Can be used for the stop event. Updating only the column sort order
	 */ 
	var updateColumn = function(event, ui) {
			var toColumnId = $(this).attr('id').split('_')[1];
		var taskId = $(ui.item).attr('id').split('_')[1]; 
		$.ajax({
			type: 'POST',
			url: updateSortorderUrl+'/'+toColumnId,
			data: {
			    taskid: taskId,
				order: $(this).sortable("toArray")
			}
		});
	}
		
	//Sortable definition for the connected columns	
	$(".column > ul").each(function(index) {			
		$(this).sortable({			
			//Connect the current sortable only with the next column
			//connectWith:'.column ul:gt('+index+'):first',
			connectWith:'.column ul',
			appendTo: 'body',
			containment:"#board",
			cursor:"move",
			distance:30,
			opacity:0.7,
			placeholder:'ui-effects-transfer',
			receive: updateConnectedColumn,
			stop: updateColumn
			
		});
	});	
	
	//Update once on document load time - from utils.js
	setElementCountOnColumn();	 
			
});