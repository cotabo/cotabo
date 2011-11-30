<%@ page import="org.cotabo.Board" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />        
        <meta name="layout" content="main" />                
        <title>Cotabo - archive of ${boardInstance.name} </title>
    </head>
    <body>
  		<g:if test="${flash.message}">
	       <g:render template="/info" model="[message:flash.message]"/>	       
	    </g:if>
	    <ul>
	    <g:each in="${taskList}" var="task">
	    	<li>${task.name}</li>
	    </g:each>
	    </ul>
	     <g:paginate total="${taskTotal}" id="${boardInstance.id}" />	         
    </body>
</html>