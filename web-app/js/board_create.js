/**
 * Contains script source for the /board/create page
 */
 $(document).ready(function(){
	
	//The save button of the form at all
	$(".save").button({
		icons: {
			primary: "ui-icon-locked"
		}
	});
	//The column telete button
	$(".delete").button({
		icons: {
			primary: "ui-icon-trash"
		},
		text: false
	}).live('click', function() {
		$(this).parents("tr").remove();
	});
	
	$(".move").button({
		icons: {
			primary: "ui-icon-arrowthick-2-n-s"
		},
		text:false
	});
	
	
	//The column add button        	
	var rowTpl = function() {
		return [ 
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
					"input", {type: "radio", name:"columns["+this.columnIndex+"].workflowStartColumn"}
	    		],
	    		"td", {},[
	    			"a", {href:'#', class:'delete'}, 'delete this column definition'
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
			columnIndex: $("#columns_content > table > tbody > tr").size() -1
		};
		var tpl = $("#columns_content > table > tbody").tplAppend(tplData, rowTpl);
		$("#columns_content > table > tbody > tr:last > td:last > a").button({
			icons: {
				primary: "ui-icon-trash"
			},
			text:false        		
		});        		   	
	});
	
	$("#tabs").tabs();
	
	$('.close_button').live('click', function(event) {
		$(this).parents('li').remove()
	});
	
 });