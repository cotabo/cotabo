/**
 * Holding all (mostly jQuery-ui) objects and their events that are 
 * build up onload.
 */
jQuery(function(){
	
	/********************************************************
	 * tooltips
	 *********************************************************/
	$('.avatar').twipsy();
	$('.head_color').twipsy();
	
	/********************************************************
	 * Submit all forms by ajax submit and close the dialog if 
	 * it's surrounded by one
	 *********************************************************/	
	$('form').live('submit', function(e) {
		var that = this
		$.ajax({		
			type: 'POST',
			url: $(this).attr('action'),
			data: $(this).serialize(),
			success: function() {
				$(that).parents('.modal').modal('hide');
			},
			error: function(jqXHR, textStatus, errorThrown) {					
				$(that).parents('.modal-body').prepend(jqXHR.responseText);
			}
		});					
		//Don't really submit
		return false;	
	 });	
	
	/********************************************************
	 * Section for the equal height of all task columns.
	 *********************************************************/
	$(document).ready(function() {
		equalHeight($('div.column ul'));
	});
	
	
    /********************************************************
	 * Section for the client-side search.
	 *********************************************************/	
	$('input#search').quicksearch('li.task');
	
	/********************************************************
	 * Section for the keyboard navigation.
	 * The check whether the event comes from the HTMLInputElement is absolutely necessary.
	 ********************************************************/	 
	var activateShortcuts = function(e) {		
		switch(e.which) {
	   		case 78: // 'n'
	   			$('button#b_new_task').click();
	   			e.preventDefault();
	   		 	return false;
	   			break;		   			
	   		case 69: // 'e'
	   			fn_expand.call();
	   			e.preventDefault();
	   		 	return false;
	   			break;
	   		case 67 : // 'c'
	   			fn_collapse.call();
	   			e.preventDefault();
	   		 	return false;
	   			break;
	   		case 84: // 't'	   			
	   			$('button#b_new_tag').click();
	   			e.preventDefault();
	   		 	return false;
	   			break;
	   		default:
	   			return true;
		}
	}
	//onLoad activate shortcuts
	$(document).bind('keyup', activateShortcuts );
	//deactivate on dialog openings	
	$('.modal').live('show', function() {
		$(document).unbind('keyup');
	});
	//and activate on dialog closes again
	$('.modal').live('hidden', function() {
		$(document).bind('keyup', activateShortcuts );
	});

	/********************************************************
	 * Section for the Board menu and everything that belongs to it.
	 * refer: board/_menu template
	 ********************************************************/	

	var fn_collapse = function() {		
		var matched = $('#board > div.column > ul > li > div.task-header > img.expander')		 		 	
		for (var i=0;i< matched.size();i++)	{
			collapse(matched[i]);
		}
		return true;
	};
	$('#b_collapse').click(fn_collapse);

	var fn_expand = function() {	
		var matched = $('#board > div.column > ul > li > div.task-header > img.expander')	
		for (var i=0;i< matched.size();i++)	{
			expand(matched[i]);			
		}
		return true;
	};
	$('#b_expand').click(fn_expand);
	
	
	/********************************************************
	 * Section for Task - DOM related events
	 * refer: board/show view
	 * refer: taglib/BoardTagLib
	 ********************************************************/
	var expand = function(expanderElement) {			
		$(expanderElement).attr('src', $(expanderElement).attr('src').replace(/down/, "up"));
		$(expanderElement).parent('div').next('div').css('display', 'block');
	}
	var collapse = function(expanderElement) {		
		$(expanderElement).attr('src', $(expanderElement).attr('src').replace(/up/, "down"));
		$(expanderElement).parent('div').next('div').css('display', 'none');
	}
	
	/**
	 * Handler for expanding / collapsing tasks on click of the exapnd icon.	 
	 */	
	var handleClickHeader = function(event) {	
		var src = $(this).attr('src');
		if(src.search(/down/) > -1) {
			expand(this);
		}
		else {
			collapse(this)
		}		
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
		var wasBlocked = !$(this).hasClass('greyed-out');                
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
	$('img.archive').live('click', function(eventObject) {
		//Fire a get request to the archive action
		$.get(archiveUrl + '/' + $(this).parents('.task').attr('id').split('_')[1]);		
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
	         success: function(data, textStatus, jqXHR) {
        		//This also evaluates the contained script elements
        		$('body').append(data); 
        		$('#task_update_dialog').modal({backdrop:true, keyboard:true, show:true});
        		$('#task_update_dialog').bind('hidden', function() {
        			$(this).remove();
        		});
	        }
	     });			                     			     			     			     			 
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
