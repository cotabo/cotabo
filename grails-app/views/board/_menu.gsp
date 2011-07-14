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
		console.time('native');		
		var matched = $('#board > div.column > ul > li > div.task-header > span.ui-icon-carat-1-n')		 		 	
		for (var i=0;i< matched.size();i++)	{
			$(matched[i]).click();		
		}
		console.timeEnd('native'); 
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
	}).parent().buttonset();
	
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
    <div id="new_task_helper"></div>	
	<span id="toolbar">	   
		<button id="b_new_task">new task</button>
		<button id="b_collapse">collapse all tasks</button>
		<button id="b_expand">expand all tasks</button>
	</span>	
</content>

<g:render template="/task/create"/>