<jq:jquery>
	/**
	 * Button behavior
	 */
	$('#b_create_task').button({
		icons: {
			primary: 'ui-icon-document'
		}		
	}).click(function() {
		$('#createTaskForm').dialog('open');
	});	
	
	/**
	 * Dialog with form content.
	 */
	$('#createTaskForm').dialog({
		autoOpen: false,
		height: 450,
		width: 450,
		modal: true,
		buttons: {
			"create task": function() {
				resetDialogErrors('#createTaskForm');				
				$('#taskForm').submit();				
			},			
			"cancel": function() {
				$( this ).dialog( "close" );
			}
		},
		close: function() {
			resetDialogErrors('#createTaskForm');		
		}
	});
	
</jq:jquery>
<div id="board_menu" class="ui-widget-content">
	<button id="b_create_task">new task</button>
</div>
<g:render template="/task/create"/>