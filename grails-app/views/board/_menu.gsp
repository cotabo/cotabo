<jq:jquery>
	$('#b_create_task').button({
		icons: {
			primary: 'ui-icon-document'
		}		
	}).click(function() {
		$('#createTaskForm').dialog('open');
	});
	
	$('#createTaskForm').dialog({
		autoOpen: false,
		height: 400,
		width: 450,
		modal: true,
		buttons: {
			"create task": function() {
				//$('#task_create_form').submit();
				//TODO: need to ajax submit & validate this form 
				//try validation with http://code.google.com/p/jquery-validation-ui-plugin/
			},			
			"cancel": function() {
				$( this ).dialog( "close" );
			}
		},
		close: function() {
			$('#createTaskForm :input').val("");
		}
	});
	
</jq:jquery>
<div id="board_menu" class="ui-widget-content">
	<button id="b_create_task">new task</button>
</div>
<g:render template="/task/create"/>