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
	});	
	
	$('#b_collapse').button({
		icons: {
			secondary: 'ui-icon-carat-1-n'
		}
	}).click(function() {		
		$('.column .task-header .ui-icon').each(function() {			
			if ($(this).hasClass('ui-icon-carat-1-n')) {
				$(this).click();
			}
		});		
		return true;
	});
	
	$('#b_expand').button({
		icons: {
			secondary: 'ui-icon-carat-1-s'
		}
	}).click(function() {		
		$('.column .task-header .ui-icon').each(function() {			
			if ($(this).hasClass('ui-icon-carat-1-s')) {
				$(this).click();
			}
		});		
		return true;
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
<content tag="toolbar">	
	<span id="toolbar">
		<button id="b_new_task">new task</button>
		<button id="b_collapse">collapse all tasks</button>
		<button id="b_expand">expand all tasks</button>
	</span>	
</content>

<g:render template="/task/create"/>