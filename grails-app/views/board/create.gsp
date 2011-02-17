

<%@ page import="app.taskboard.Board" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />        
        <link rel="stylesheet" href="${resource(dir:'css',file:'create.css')}" />
        <title>Taskboard - create board</title>
        <jq:jquery>     
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
		        		"td", {},[
		        			"a", {href:'#', class:'delete'}, '&nbsp;'
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
        </jq:jquery>
        
    </head>
    <body>	
    	<h1>New Taskboard</h1>		   
	    <g:if test="${flash.message}">
	    <div class="message">${flash.message}</div>
	    </g:if>
	    <g:hasErrors bean="${boardInstance}">
	    <div class="ui-state-error ui-corner-all">
	        <g:renderErrors bean="${boardInstance}" as="list" />
	    </div>
	    </g:hasErrors>		
	     <g:form action="save" >    	       
	    <div id="tabs">	    
		    	<ul>
		    		<li><a href="#board_details">Board information</a></li>
		    		<li><a href="#columns">Column definition</a></li>
		    		<li><a href="#colors">Task colors</a></li>
		    		<li><a href="#priorities">Priorities</a></li>
		    		
		    	</ul>	    				 	
				<div id="board_details">   
					<table>
					    <tbody>                        
					        <tr>
					            <td>
					                <label for="name">Name</label>
					            </td>
					            <td>
					                <g:textField name="name" maxlength="25" value="${boardInstance?.name}" />
					            </td>
					        </tr>
					    
					        <tr>
					            <td>
					                <label for="description">Description</label>
					            </td>
					            <td>
					                <g:textArea name="description" cols="40" rows="5" value="${boardInstance?.description}" />
					            </td>
					        </tr>
					    </tbody>
					</table>
		     	</div>	
		     	     	
		     	<div id="columns">
		     		<div id="columns_content" >	
			     		<table>
			     			<tbody>
			     				<tr>		     					
			     					<th>Name</th>
			     					<th>Short description</th>
			     					<th>Task limit</th>
			     					<th>&nbsp;</th>
			     				</tr>
			     				<g:each in="${boardInstance.columns}" var="column" status="i">
		     					<tr class="column">
		     						<td>
		     							<g:textField name="columns[$i].name" maxlength="75" value="${boardInstance?.columns[i]?.name}" />
		     						</td>
		     						<td>
		     							<g:textField name="columns[$i].description" size="50" value="${boardInstance?.columns[i]?.description}" />
		     						</td>
		     						<td>
		     							<g:textField name="columns[$i].limit" size="2" maxlength="2" value="${boardInstance?.columns[i]?.limit}" />
		     						</td>
		     						<td>
		     							<a href="#" class="delete">&nbsp;</a>	
		     						</td>
		     					</tr>	     					
			     				</g:each> 
			     			</tbody>
			     		</table>				     					
						<button class="add" type="button">add column</button>		
	                </div>    
				</div>	
				
			    <div id="colors">
					<g:render template="/color/listAndSelect"/>
				</div>				
			
				<div id="priorities">
				</div>

			 
		</div>    
		<div id="submit_div">
			<button class="save" type="submit">create board</button>
		</div>
		</g:form>	
    </body>
</html>
