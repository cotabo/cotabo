<%@ page import="org.cotabo.Board" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />        
        <meta name="layout" content="main" />                
        <title>Cotabo - <sec:loggedInUserInfo field="username"/>'s boards</title>
    </head>
    <body>       
 		<content tag="herounit.hook">
	    	<div class="hero-unit">
	 			<p>
		            Here are the 'Cotabo's that you either own or where you participate in.
		        <p>		        
	        </div>
	   	</content>    	     	        	  
	    <g:if test="${flash.message}">
	       <g:render template="/info" model="[message:flash.message]"/>	       
	    </g:if>
	    
        <g:render template="import_export_menu"/>
         
        <h2>your boards</h2>
        <div class="row">
	        <g:each in="${adminBoards}" status="i" var="boardInstance">
	        <g:render template="/board/summary" model="['admin':true,'boardInstance':boardInstance]"/>            
	        </g:each>  
        </div>        
                
                
        <ul class="board_list">   
            <li><h2>participating boards</h2></li>
            <g:each in="${userBoards}" status="i" var="boardInstance">
            <g:render template="/board/summary" model="['admin':false,'boardInstance':boardInstance]"/>            
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
