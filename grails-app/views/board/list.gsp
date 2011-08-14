<%@ page import="org.cotabo.Board" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />        
        <meta name="layout" content="main" />                
        <title>Cotabo - <sec:loggedInUserInfo field="username"/>'s boards</title>
    </head>
    <body>                	    
	    <g:if test="${flash.message}">
	       <div class="message">${flash.message}</div>
	    </g:if>
        
        <ul class="board_list">   
            <li><h2>your boards</h2></li>
            <g:each in="${adminBoards}" status="i" var="boardInstance">
            <tb:boardSummary board="${boardInstance}" admin="${true}"></tb:boardSummary>
            </g:each>      
        </ul>
                
                
        <ul class="board_list">   
            <li><h2>participating boards</h2></li>
            <g:each in="${userBoards}" status="i" var="boardInstance">
            <tb:boardSummary board="${boardInstance}" admin="${false}"></tb:boardSummary>
            </g:each>      
        </ul>
        <jq:jquery>
            $('span.ui-icon').mouseover(function() {
                $(this).addClass('ui-state-hover');
            }).mouseout(function() {
                $(this).removeClass('ui-state-hover');
            });
        </jq:jquery> 
    </body>    
</html>
