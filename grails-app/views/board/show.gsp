<%@ page import="org.cotabo.Board" %>
<%@ page import="org.cotabo.TaskColor" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />        
        <link rel="stylesheet" href="${resource(dir:'css',file:'jquery.pnotify.default.css')}" />
        <link rel="stylesheet" href="${resource(dir:'css',file:'board.css')}" />               
        <title>Cotabo - ${boardInstance?.name}</title>
        <script type="text/javascript" src="${resource(dir:'js/jquery-plugins', file:'jquery.flydom-3.1.1.js')}"></script>
	    <script type="text/javascript" src="${resource(dir:'js/jquery-plugins', file:'jquery.flydom.extension.js')}"></script>
	    <script type="text/javascript" src="${resource(dir:'js/jquery-plugins', file:'jquery.atmosphere.js')}"></script>	    
	    <script type="text/javascript" src="${resource(dir:'js/jquery-plugins', file:'jquery.quicksearch.js')}"></script>	         	      
	    <script type="text/javascript" src="${resource(dir:'js', file:'utils.js')}"></script>
	    <script type="text/javascript" src="${resource(dir:'js', file:'objectsandevents.js')}"></script>
	    <script type="text/javascript" src="${resource(dir:'js', file:'atmospherehandling.js')}"></script>
        <script type="text/javascript">
            /**
             * Global variable definition
             */
             var atmosphereSubscriptionUrl = '${resource(dir: '/atmosphere/boardupdate?boardId=') + boardInstance.id}';
             var moveTaskUrl = '${createLink(controller:"task", action:"move")}';
             var reorderTaskUrl = '${createLink(controller:"task", action:"reorder")}';
             var updateTaskUrl = '${createLink(controller:'task', action:'update')}';
             var editTaskUrl = '${createLink(controller:'task', action:'edit')}';
             var chatUrl = '${createLink(controller:"board", action:"chat")}';
             var avatarUrl = '${createLink(controller:'user', action:'avatar')}';
        </script>
    </head>
    <body>
    
        <content tag="header">
    	<form class="task_search">
		    <input type="text" id="search">
		    <span style="margin:4px;"class="ui-icon ui-state-default ui-icon-search"></span>
		</form>	   
		</content> 	
		
		<g:render template="menu"/>
		<g:render template="show" model="['boardInstance':boardInstance]"/>
    	<g:render template="/color/list" model="${['colors':boardInstance.colors.sort{a,b -> a.name <=> b.name}]}"></g:render>      	    
    
	    <div id="error_dialog"></div>
	    <jq:jquery>
	    /**
	     * Global AJAX error handling.
	     * Always expecting JSON containing {"title":"error_title", "message":"error_message"}
	     */
	    $.ajaxSetup({
	        error: function(jqXHR, textStatus, errorThrown) {                    
	            var message 
	            var title = ''
	            try {       
	                var data = $.parseJSON(jqXHR.responseText);
	                title = data.title;
	                message = data.message
	            }
	            catch(e) {
	                if(jqXHR.responseText != null && jqXHR.responseText != '') {
	                    message = jqXHR.responseText
	                }
	                else {
	                    message = 'Server seems not to be available. Please refresh the site to be sure.'
	                }
	            }
	            $.pnotify({
	                pnotify_title: title,
	                pnotify_text: message,                  
	                pnotify_shadow: true,           
	                pnotify_type: 'error'           
	            });
	        }
	    });
	    </jq:jquery>
    </body>
</html>
