
<%@ page import="org.cotabo.Board" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />        
        <meta name="layout" content="main" />                
        <title>Cotabo - available boards</title>
    </head>
    <body>                	    
	    <g:if test="${flash.message}">
	    <div class="message">${flash.message}</div>
	    </g:if>	   
	    
	    <h2>your boards</h2> 
        <table class="list">
            <thead>
                <tr class="ui-state-default">       
                	<th>Name</th>
                	<th>Description</th>
                	<th>Admins</th>                         
                </tr>
            </thead>
            <tbody>
            <g:each in="${adminBoards}" status="i" var="boardInstance">
                <tr class="${(i % 2) == 0 ? 'odd' : 'even'} ">                	                                               
                    <td>
                        <g:link action="show" id="${boardInstance.id}" class="highlighted_link">
                    		${fieldValue(bean: boardInstance, field: "name")}
                    	</g:link>
                    </td>
                
                    <td>${fieldValue(bean: boardInstance, field: "description")}</td>
                    
                    <td>
                    	<g:each status="a" in="${boardInstance.admins}" var="admin">
                   		${admin.firstname.encodeAsHTML()} ${admin.lastname.encodeAsHTML()}<g:if test="${a >= 0 && a < boardInstance.admins.size() -1}">, </g:if>
                    	</g:each>
                    </td>
                </tr>
            </g:each>
            </tbody>
        </table>
        
        <h2>participating boards</h2> 
        <table class="list">
            <thead>
                <tr class="ui-state-default">       
                    <th>Name</th>
                    <th>Description</th>
                    <th>Admins</th>                         
                </tr>
            </thead>
            <tbody>
            <g:each in="${userBoards}" status="i" var="boardInstance">
                <tr class="${(i % 2) == 0 ? 'odd' : 'even'} ">                                                                 
                    <td>
                        <g:link action="show" id="${boardInstance.id}" class="highlighted_link">
                            ${fieldValue(bean: boardInstance, field: "name")}
                        </g:link>
                    </td>
                
                    <td>${fieldValue(bean: boardInstance, field: "description")}</td>
                    
                    <td>
                        <g:each status="a" in="${boardInstance.admins}" var="admin">
                        ${admin.firstname.encodeAsHTML()} ${admin.lastname.encodeAsHTML()}<g:if test="${a >= 0 && a < boardInstance.admins.size() -1}">, </g:if>
                        </g:each>
                    </td>
                </tr>
            </g:each>
            </tbody>
        </table>           
    </body>
</html>
