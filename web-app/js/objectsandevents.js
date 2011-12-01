/**
 * Holding all (mostly jQuery-ui) objects and their events that are 
 * build up onload.
 */
jQuery(function(){
	
	
    /********************************************************
	 * Section for the client-side search.
	 *********************************************************/	
	$('input#search').quicksearch('li.task');	
	
	/********************************************************
	 * Section for the keyboard navigation.
	 * The check whether the event comes from the HTMLInputElement is absolutely necessary.
	 ********************************************************/	 
	$(document).keypress(function(e) {
		var srcElement = e.srcElement ? e.srcElement : e.target; // Firefox fix, worked in Chrome and Safari
		var dialogs = $('.ui-dialog:visible').toArray();
		var shortcutsDisabled = (dialogs.length > 0 ? true : false) || (srcElement instanceof HTMLInputElement);;
		var code = (e.which ? e.which : e.keyCode);
		if(!shortcutsDisabled) {		   
			switch(code) {				
		   		case 110: // 'n'
		   			$('#createTaskForm').dialog('open');
		   			e.preventDefault();
		   		 	return false;
		   			break;		   			
		   		case 101: // 'e'
		   			fn_expand.call();
		   			e.preventDefault();
		   		 	return false;
		   			break;
		   		case 99 : // 'c'
		   			fn_collapse.call();
		   			e.preventDefault();
		   		 	return false;
		   			break;
		   		case 116: // 't'
		   			$('#tags').dialog('open');
		   			e.preventDefault();
		   		 	return false;
		   			break;
		   		default:
		   			return true;		   		
		   }
		}
	});     
		
	/********************************************************
	 * Section for the Board menu and everything that belongs to it.
	 * refer: board/_menu template
	 ********************************************************/	
	/**
	 * Button definitions
	 */
	$('#b_new_task').click(function() {
		$('#createTaskForm').dialog('open');
		return true;
	});
	
	$('#b_new_tag').click(function() {
		$('#tags').dialog('open');
		return true;
	});
	
	var fn_collapse = function() {		
		var matched = $('#board > div.column > ul > li > div.task-header > img.expander')		 		 	
		for (var i=0;i< matched.size();i++)	{
			$(matched[i]).click();		
		}
		return true;
	};
	$('#b_collapse').click(fn_collapse);

	var fn_expand = function() {	
		var matched = $('#board > div.column > ul > li > div.task-header > img.expander')	
		for (var i=0;i< matched.size();i++)	{
			$(matched[i]).click();
		}
		return true;
	};
	$('#b_expand').click(fn_expand);
	
	
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
	$('.task-header .expander').live('click', handleClickHeader);
	
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
	 * Handles click events on the archive button
	 */
	$('a.archive').live('click', function(eventObject) {
		//Fire a get request to the archive action
		$.get($(this).attr('href'));
		eventObject.preventDefault();
		return false;
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
	 * refer js/utils.js
	 ********************************************************/	
	//Sortable definition for the connected columns	
	$(".column > ul").each(function(index) {			
		applySortable(this);
	});				
});
