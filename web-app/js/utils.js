/**
 * Utility functions for general reuse
 */

/**
 * resets the state of a dialog (removing errors etc.)
 * 
 * @param dialogSelector: The selector for the dialog container element.
 */
var resetDialogErrors = function(dialogSelector) {
	$(dialogSelector+' :input').removeClass('error');
	$(dialogSelector+' div.ui-state-error').remove();
}

/**
 * Checks for errors in a JSON response of a validated Grails domain object.
 * It prints error messages and applies the .error class to all invalid input elements 
 * (assuming they have the same name as the class property).
 * 
 * @params responseData: the response data from the server as evaluated JSON object.
 * @params formContainerSelector: the selector for the container in which the form sits in (e.g. a div representing a jQuery dialog).
 * 
 * @returns true if the request was successfull or false when errors appeared during server side validation.
 */
var checkForSuccess = function(responseData, formContainerSelector) {	
	if(responseData.errors != null) {
		$(formContainerSelector).prepend('<div class="ui-state-error ui-corner-all"></div>');
		$(responseData.errors).each(function(index, element) {
			$(formContainerSelector + ' > .ui-state-error').append('<p><span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-alert"></span>' + element.message + '</p>');
			$(formContainerSelector + ' :input[name="'+element.field+'"]').addClass('error');
		});
		return false;
	}
	else {
		return true;
	}
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
	return [ 
		"li", {class:'ui-widget', id:'task_'+this.id}, [
			"div", {class:'task-header ui-state-default'}, [
				"div", {}, this.name,
				"span", {class:'ui-icon ui-icon ui-icon-arrowthickstop-1-n'}
			],
			"div", {class:'task-content ui-widget-content', style:'background:'+this.color}, [
				"table", {}, [
					"tbody", {}, [
						"tr", {}, [
							"td", {}, [
								"b", {}, 'Description'									
							],
							"td", {}, this.description								
						],
						"tr", {}, [
							"td", {}, [
								"b", {}, 'Priority'									
							],
							"td", {}, this.priority								
						],
						"tr", {}, [
							"td", {}, [
								"b", {}, 'Assignee'									
							],
							"td", {}, this.assignee		
						]
					]
				]
			]    		
		]
	];
}