

<%@ page import="org.cotabo.Board" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />        
        <link rel="stylesheet" href="${resource(dir:'css',file:'board_create.css')}" />
        <script type="text/javascript" src="${resource(dir:'js/jquery-plugins', file:'jquery.flydom-3.1.1.js')}"></script>        
        <script type="text/javascript" src="${resource(dir:'js', file:'board_create.js')}"></script>
        <title>Cotabo - create new board</title>
    </head>
    <body>	
    	<h1>new taskboard</h1>		   
	    <g:if test="${flash.message}">
	    <div class="message">${flash.message}</div>
	    </g:if>
	    <g:hasErrors bean="${boardInstance}">
	    <div class="ui-state-error ui-corner-all">
	        <g:renderErrors bean="${boardInstance}" as="list" />
	    </div>
	    </g:hasErrors>		
	    <g:form action="save">    	       
	    <div id="tabs">	    
		    	<ul>
		    		<li><a href="#board_details">board information</a></li>
		    		<li><a href="#columns">column definition</a></li>
		    		<li><a href="#usermanagement">user management</a></li>
		    	</ul>	    				 	
				<div id="board_details"> 
					<g:render template="/info" model="[messagecode: 'board.create.description']"/>  
					<g:render template="/board/create"/>
		     	</div>	
		     	<div id="columns">
		     		<g:render template="/info" model="[messagecode: 'board.create.columns.description']"/>
		     		<g:render template="/column/create"/>
				</div>		
				<div id="usermanagement">
				    <g:render template="/info" model="[messagecode: 'board.create.usermanagement']"/>
				    <g:render template="/board/usermanagement"/>
				</div>	 
		</div>    
		<div id="submit_div">
			<button class="save" type="submit">create board</button>
		</div>
		</g:form>	
    </body>
</html>
