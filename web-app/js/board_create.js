/**
 * Contains script source for the /board/create page
 */
 $(document).ready(function(){
	
	$('.delete').live('click', function() {
		var row = $(this).parents('.clearfix');		
		var index = row.index();		
		var form = row.parents('form');
		form.append('<input type="hidden" name="deleteColumn" value="'+index+'"/>');					
		row.remove();
	});	
	
	$(".add_column").click(function(event) {		
		var tplData = {
			columnIndex: $("#columns > .clearfix > .row").length
		};
		$("#columns").tplAppend(tplData, rowTpl);
	});
	
	$("#columns").sortable({
		stop: updateColumnNameOrdering
	});
	
	var updateColumnNameOrdering = function() {
		var columns = $("#columns > .clearfix > .row").toArray();		
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
			
	/**
	 * Section for usermanagement
	 */
	$('#list_selectable, .user_droppable').sortable({
		connectWith:".user_list",
		over: function() {
			if ($(this).hasClass('user_droppable')) {
				$(this).addClass('highlight');
			}
		},
		out: function() {
			if ($(this).hasClass('user_droppable')) {
				$(this).removeClass('highlight');
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
	}).disableSelection();	
	
	
	//the FlyDom template for a row that represents a column 	
	var rowTpl = function() {
		return [ 
		    "div", {class:'clearfix'}, [
			    "div", {class:'row'}, [
			        "span", {class:'span4'}, [
			            "input", {type: "text", name:"columns["+this.columnIndex+"].name", class:'span4', maxlength:'75'}
			         ],
			         "span", {class:'span5'}, [
			            "input", {type: "text", size: 50, name:"columns["+this.columnIndex+"].description", class:'span5'}
			         ],
			         "span", {class:'span2'}, [
	                    "input", {type: "text", size: 2, name:"columns["+this.columnIndex+"].limit", value:"0", class:'span2'}
			         ],
			         "span", {class:'span2'}, [
			            "input", {type: "radio", name:"workflowStart", id:"workflowStart", value:this.columnIndex}
			         ],
			         "span", {class:'span2'}, [
	                    "input", {type: "radio", name:"workflowEnd", id:"workflowEnd", value:this.columnIndex}
		             ],
		             "span", {class:'span1'}, [
		                "img", {class:'icon delete', title:'delete this column', src:'/cotabo/images/icons/trash_stroke_12x12.png'}
		             ]	             
		        ]
		    ]
	    ]
	};        	
	
 });