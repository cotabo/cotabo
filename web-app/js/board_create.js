/**
 * Contains script source for the /board/create page
 */
 $(document).ready(function(){
	
	//The save button of the form at all
	$(".save").button({
		icons: {
			primary: "ui-icon-disk"
		}
	});
	//The column telete button
	$(".delete").button({
		icons: {
			primary: "ui-icon-trash"
		},
		text: false
	}).live('click', function() {
		var li = $(this).parents("li");
		var index = li.index();
		var ul = $(li).parents("ul");
		ul.append('<input type="hidden" name="deleteColumn" value="'+index+'"/>');					
		li.remove();
		
	});
	
	$(".move").button({
		icons: {
			primary: "ui-icon-arrowthick-2-n-s"
		},
		text:false
	});
	
	
	//the FlyDom template for a row that represents a column 	
	var rowTpl = function() {
		return [ 
		    "li", {}, [
		        "table", {}, [
		            "colgroup", {}, [
		                "col", {width:"25%"}, ,
		                "col", {width:"35%"}, ,
		                "col", {width:"20%"}, ,
		                "col", {width:"10%"}, ,
		                "col", {width:"10%"}
		            ],
		        	"tbody", {}, [
						"tr", {}, [
							"td", {}, [
								"input", {type: "text", name:"columns["+this.columnIndex+"].name"}
				    		],
					        "td", {}, [
								"input", {type: "text", size: 50, name:"columns["+this.columnIndex+"].description"}
				    		],
					  		"td", {}, [
								"input", {type: "text", size: 2, name:"columns["+this.columnIndex+"].limit", value:"0"}
				    		],
					  		"td", {}, [
								"input", {type: "radio", name:"workflowStart", id:"workflowStart", value:this.columnIndex} 
				    		],
				    		"td", {},[
				    			"a", {href:'#', class:'delete'}, 'delete this column definition'
				    		]
						]
					]
				]
			]
		]
	};        	
	$(".add").button({
		icons: {
			primary: "ui-icon-plusthick"
		}
	}).click(function(event) {
		        		
		var tplData = {
			columnIndex: $("ul.column_list > li").length
		};
		var tpl = $("ul.column_list").tplAppend(tplData, rowTpl);
		$("ul.column_list > li > table > tbody > tr:last > td:last > a").button({
			icons: {
				primary: "ui-icon-trash"
			},
			text:false        		
		});        		   	
	});
	
	var updateColumnNameOrdering = function() {
		var columns = $("ul.column_list > li").toArray();		
		for (var i = 0; i < columns.length; i++) {
			var fields = $(columns[i]).find(":text").toArray();
			for (var j = 0; j < fields.length; j++) {
				var oldName = $(fields[j]).attr('name');
				var newName = oldName.replace(/\d/, i);
				//Setting the name and the ID to the new name
				$(fields[j]).attr('name', newName);
				$(fields[j]).attr('id', newName);
			}			
			var radio = $(columns[i]).find(":radio")			
			radio.attr('value', i);			
		}		
	}
	$("ul.column_list").sortable({
		stop: updateColumnNameOrdering
	});
	
	$("#tabs").tabs();
	
	$('.close_button').live('click', function(event) {
		$(this).parents('li').remove()
	});
	
	/**
	 * Section for usermanagement
	 */
	$('#list_selectable, .user_droppable').sortable({
		connectWith:".user_list",
		over: function() {
			if ($(this).hasClass('user_droppable')) {
				$(this).addClass('ui-state-highlight');
			}
		},
		out: function() {
			if ($(this).hasClass('user_droppable')) {
				$(this).removeClass('ui-state-highlight');
			}
		},
		stop: function(event, ui) {
			var id = $(ui.item).parent().find('li').size() -1;
			var role = $(ui.item).parent().attr('id');
			var hidden = $(ui.item).find('div > input[type="hidden"]');
			if (role != 'list_selectable') {				
				//Giving the hidden form fiels the correct name
				hidden.attr('name', role);
				//hidden.attr('id', role+'['+id+'].id');				
			}
			else {				
				//The initial state for users that don't have a role assigned				
				hidden.attr('name', 'user['+id+']');
				//hidden.attr('id', 'user['+id+']');
			}
			
		}
	}).disableSelection();;	
 });