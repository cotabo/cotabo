<%@ page import="org.cotabo.Board" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />        
        <link rel="stylesheet" href="${resource(dir:'css',file:'board.css')}" />
        <script type="text/javascript" src="${resource(dir:'js', file:'objectsandevents.js')}"></script>
        <script type="text/javascript" src="${resource(dir:'js', file:'atmospherehandling.js')}"></script>
        <title>Cotabo - ${boardInstance?.name}</title>
        <script type="text/javascript">
            /**
             * Global variable definition
             */
             var atmosphereSubscriptionUrl = '${resource(dir: '/atmosphere/boardupdate?boardId=') + boardInstance.id}'
             var moveTasksUrl = '${createLink(controller:"column", action:"updatetasks")}'
             var updateSortorderUrl = '${createLink(controller:"column", action:"updatesortorder")}'
             var updateTaskUrl = '${createLink(controller:'task', action:'update')}'
             var editTaskUrl = '${createLink(controller:'task', action:'edit')}'
             var chatUrl = '${createLink(controller:"board", action:"chat")}'
        </script>
    </head>
    <body>    	
    	<tb:board board="${boardInstance}">
    		<g:render template="menu"/>
    		<g:each in="${boardInstance.columns}" var="column">
    		<tb:column column="${column}">
    			<g:each in="${column.tasks}" var="task">
    			<g:if test="${(column == boardInstance.columns.first()) || (column == boardInstance.columns.last()) }">
    				<g:set var="hide" value="${true}" />  				
    			</g:if>
    			<g:else>
    				<g:set var="hide" value="${false}" />
    			</g:else>
    			<tb:task task="${task}" hide="${hide}"/>
    			</g:each>
    		</tb:column>
    		</g:each>
    	</tb:board>      	    
    </body>
</html>
