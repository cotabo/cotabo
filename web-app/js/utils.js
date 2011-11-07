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
	var fromColumnId = $(ui.sender).parents('[id^="column_"]').attr('id').split('_')[1];
	var taskId = $(ui.item).attr('id').split('_')[1];
								
	//Post onto controller "column" and action "updatetasks"
	$.ajax({
		type: 'POST',
		url: moveTasksUrl,
		data: {
			'fromColumn': fromColumnId, 
			'toColumn': toColumnId,
			'taskid': taskId,
		},
		success: function(data) {
			//Update the task counts on each column
			setElementCountOnColumn();
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

/**
 * Representing a skeleton task object that can be rendered 
 * with the flydom plugin for jQuery (http://dohpaz.com/flydom/).
 * The main purpose is to use it with the tplAppend / tplPrepend function.
 * 
 * This is basically the same as the tag tb:task renders but as
 * page rendering mechanisms can not be used with AJAX request we need to maintain
 * the task structure here and inside the BoardTagLib. 
 * 
 */
var taskTpl = function () {
	var theId = this.id
	var blockedClass
	var blockedTitle
	if (this.blocked) {
		blockedClass='ui-icon-locked blocked';
		blockedTitle='blocked';
	}
	else {
		blockedClass='ui-icon-unlocked not-blocked';
		blockedTitle='unblocked';
	}
	
	return [ 
		"li", {class:'ui-widget ui-corner-all', id:'task_'+this.id}, [
			"div", {class:'task-header ui-state-default'}, [
			    "img", {class:'ui-icon ui-icon-person avatar', src:avatarUrl+'/'+this.assignee, title:this.assignee}, ,
				"div", {class:'head_color', style:'background-color:'+this.colors[0].color}, ,
				"div", {class:'head_name'}, '#' + this.id + ' - ' +this.name,
				"span", {class:'block-box ui-icon '+ blockedClass, title:blockedTitle}, ,
				"span", {class:'expander ui-icon ui-icon ui-icon-carat-1-n'}, 
			],
			"div", {class:'task-content ui-widget-content', style:'display:block;'}, [
				"table", {}, [
				    "colgroup", {}, [
				        "col", {width:'25%'}, ,
				        "col", {width:'75%'}, 
				    ],
					"tbody", {}, [	              
						"tr", {}, [
							"td", {}, [
								"b", {}, 'Description:'									
							],
							"td", {}, this.description								
						],
						"tr", {}, [
							"td", {}, [
								"b", {}, 'Details:'									
							],
							"td", {}, this.details != null ? this.details.replace(/\n/g, '<br/>') : ''							
						],
						"tr", {}, [
							"td", {}, [
								"b", {}, 'Priority:'									
							],
							"td", {}, this.priority								
						],
						"tr", {}, [
							"td", {}, [
								"b", {}, 'Assignee:'									
							],
							"td", {}, this.assignee		
						]
					]
				]
			]    		
		]
	];
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
