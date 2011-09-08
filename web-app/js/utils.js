/**
 * Utility functions for general reuse
 */

/**
 * Update all columns task counts.
 * Setting this on the window as we need it in a different script block (task creation)
 */   
var setElementCountOnColumn = function() {        		
	$(".column").each(function(index){	    			
		var size = $(this).children("ul").children("li").size();
		if (size == null) {
			size = 0 ;
		}
		//Set the current number of elements to the element count for each column
		var pValueDom = $(this).children("span").children("span").children("p.value");
		var pLimitDom = $(this).children("span").children("span").children("p.limit");
		pValueDom.text(size);
		if (pValueDom.text() == pLimitDom.text() || parseInt(pValueDom.text()) > parseInt(pLimitDom.text())) {
			$(pValueDom).addClass('red-font');
		}
		else {
			if ($(pValueDom.hasClass('red-font'))) {
				pValueDom.removeClass('red-font');
			}
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
				"div", {class:'head_color', style:'background-color:'+this.color}, ,
				"div", {id:'color_helper', style:'display:none'}, this.color,
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
