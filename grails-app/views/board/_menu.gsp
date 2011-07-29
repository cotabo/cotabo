<jq:jquery>
	/**
	 * Button behavior
	 */
	$('#b_new_task').button({
		icons: {
			secondary: 'ui-icon-document'
		}		
	}).click(function() {
		$('#createTaskForm').dialog('open');
		return true;
	});	
	
	$('#b_collapse').button({
		icons: {
			secondary: 'ui-icon-carat-1-n'
		}
	}).click(function() {
		//console.time('native');		
		var matched = $('#board > div.column > ul > li > div.task-header > span.ui-icon-carat-1-n')		 		 	
		for (var i=0;i< matched.size();i++)	{
			$(matched[i]).click();		
		}
		//console.timeEnd('native'); 
		return true;
	});
	
	$('#b_expand').button({
		icons: {
			secondary: 'ui-icon-carat-1-s'
		}
	}).click(function() {	
		console.time('native');	
		var matched = $('#board > div.column > ul > li > div.task-header > span.ui-icon-carat-1-s')	
		for (var i=0;i< matched.size();i++)	{
			$(matched[i]).click();
		}
		console.timeEnd('native'); 
		return true;
	});
	$('#b_chat').button({
	   icons: {
	       secondary:'ui-icon-comment'
	   }
	}).click(function() {
	   $('#chat_dialog').dialog('open');
	}).parent().buttonset();

	
	var postChatMessage = function() {
	    var message = $('#chat_form').serialize();
	    $.ajax({
	         type: 'POST',
	         url: '<g:createLink controller="board" action="chat"/>',
	         data: message
	    });               
	    $('#chat_dialog').find('input').val('');
	    $('#chat_dialog').dialog('close');
	    return false;
	}
	$('#chat_dialog').find('form').submit(postChatMessage);
	$('#chat_dialog').dialog({
	   autoOpen:false,
	   height: 150,
	   width: 500,
	   modal: false,
	   buttons: {
	       "send": postChatMessage,
	       "cancel": function() {
	           $('#chat_dialog').find('input').val('');
	           $('#chat_dialog').dialog('close');
	       }
	   }
	});
	
</jq:jquery>
<content tag="toolbar">
    <div id="new_task_helper"></div>	
	<span id="toolbar">	   
		<button id="b_new_task">new task</button>
		<button id="b_chat">chat message</button>
		<button id="b_collapse">collapse all tasks</button>
		<button id="b_expand">expand all tasks</button>		
	</span>	
	<div id="chat_dialog" title="Chat message">
	   <form id="chat_form">
		   <table>
		       <tbody>
		           <tr>
		               <td><label for="message">Message:&nbsp;</label></td>
		               <td><input type="text" name="message" size="60" maxlength="254"/></td>
		           </tr>
		       </tbody>
		   </table>
	   </form>
	</div>
</content>

<g:render template="/task/create"/>