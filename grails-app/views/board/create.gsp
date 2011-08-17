<%@ page import="org.cotabo.Board" %>
<g:if test="${edit}">
    <g:set var="title" value="Cotabo - update taskboard (${boardInstance.name})"/>
    <g:set var="h1" value="update taskboard"/>
    <g:set var="button" value="update board"/> 
    <g:set var="id" value="${boardInstance.id}"/>
</g:if>
<g:else>
    <g:set var="title" value="Cotabo - create new taskboard"/>
    <g:set var="h1" value="new taskboard"/>
    <g:set var="button" value="create board"/>
    <g:set var="id" value=""/>
</g:else>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />        
        <link rel="stylesheet" href="${resource(dir:'css',file:'board_create.css')}" />
        <script type="text/javascript" src="${resource(dir:'js/jquery-plugins', file:'jquery.flydom-3.1.1.js')}"></script>        
        <script type="text/javascript" src="${resource(dir:'js', file:'board_create.js')}"></script>        
        <title>${title}</title>
    </head>
    <body>	
    	<h1>${h1}</h1>		   
	    <g:if test="${flash.message}">
	    <div class="message">${flash.message}</div>
	    </g:if>
	    <g:hasErrors bean="${boardInstance}">
	    <div class="ui-state-error ui-corner-all">
	        <g:renderErrors bean="${boardInstance}" as="list" />
	    </div>
	    </g:hasErrors>
	    

	    <g:form action="save" id="${id}">	   
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
			<button class="save" type="submit">${button}</button>
		</div>
		</g:form>	
    </body>
</html>
