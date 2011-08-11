<%@ page import="org.cotabo.Board" %>
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
	    <script type="text/javascript" src="${resource(dir:'js/jquery-plugins', file:'jquery.pnotify.min.js')}"></script>        
	    <script type="text/javascript" src="${resource(dir:'js', file:'utils.js')}"></script>
	    <script type="text/javascript" src="${resource(dir:'js', file:'objectsandevents.js')}"></script>
	    <script type="text/javascript" src="${resource(dir:'js', file:'atmospherehandling.js')}"></script>
        <script type="text/javascript">
            /**
             * Global variable definition
             */
             var atmosphereSubscriptionUrl = '${resource(dir: '/atmosphere/boardupdate?boardId=') + boardInstance.id}';
             var moveTasksUrl = '${createLink(controller:"column", action:"updatetasks")}';
             var updateSortorderUrl = '${createLink(controller:"column", action:"updatesortorder")}';
             var updateTaskUrl = '${createLink(controller:'task', action:'update')}';
             var editTaskUrl = '${createLink(controller:'task', action:'edit')}';
             var chatUrl = '${createLink(controller:"board", action:"chat")}';
             var avatarUrl = '${createLink(controller:'user', action:'avatar')}';
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
