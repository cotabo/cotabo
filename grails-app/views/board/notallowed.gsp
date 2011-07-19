<%@ page import="org.cotabo.Board" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />    
        <title>Taskboard - Error</title>    
    </head>
    <body>
    	<h4>Sorry...</h4>
    	<p>you are not allowed to see the ${boardInstance.name} board.
    	You need either be configured as a user or as an administrator. 
    	Please contact one of the administrators to configure you for usage.</p>
    	
    	<ul>
    		<g:each in="${boardInstance.admins}" var="admin">
    			<li><a href="mailto:${admin.email}">${admin.firstname} ${admin.lastname}</a></li>
    		</g:each>
    	</ul>
    </body>
</html>