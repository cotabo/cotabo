<%@ page import="org.cotabo.Board" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />        
        <meta name="layout" content="main" />                
        <title>Cotabo - <sec:loggedInUserInfo field="username"/>'s boards</title>
    </head>
    <body>                	    
	    <g:if test="${flash.message}">
	       <g:render template="/info" model="[message:flash.message]"/>	       
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
        <div style="display:none;" id="delete_dialog" title="Delete this board?">
            <p>Do you really want to delete this board?</p>
        </div>
        <jq:jquery>
            $('a.delete').click(function(event) {                
                var a = this;
                $('#delete_dialog').dialog({                
	                modal:true,
	                buttons: {
	                    "Delete" : function() {
	                        window.location = a.href;
	                        $(this).dialog("close");
	                    },
	                    "Cancel" : function() {
	                        $(this).dialog("close");
	                    }
	                }
	            });
	            event.preventDefault();
	            return false;                
            });      
            $('span.ui-icon').mouseover(function() {
                $(this).addClass('ui-state-hover');
            }).mouseout(function() {
                $(this).removeClass('ui-state-hover');
            });
        </jq:jquery> 
    </body>    
</html>
