<%@ page import="org.cotabo.Board" %>
<%@ page import="org.cotabo.TaskColor" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title>Cotabo - ${boardInstance?.name}</title>                                           
		<r:require module="board"/>    
	    
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
             var archiveUrl = '${createLink(controller:'task', action:'unarchive')}';
             var expandUrl = '${resource(dir:'images/icons',file:'arrow_down_12x12.png')}';
             var collapseUrl = '${resource(dir:'images/icons',file:'arrow_up_12x12.png')}';
        </script>
    </head>
    <body>
    	<!-- fives us the fill page width -->
    	<content tag="container.hook">-fluid</content>
        <content tag="topbar.hook">    	
        	<form class="pull-left">	
	    		<input type="text" id="search" placeholder="Search">
	    	</form>
		</content> 			
		<content tag="herounit.hook">
			<div class="hero-unit">
			<h2>${boardInstance.name?.encodeAsHTML()}</h2>
			<p class="description">${boardInstance.description?.encodeAsHTML() ?: ''}</p>
			</div>
		</content>
		<div class="row tags">
			<g:render template="show" model="['boardInstance':boardInstance, 'fake': true]"/>
		</div>
		<content tag="footer.hook">
    		<g:render template="/color/list" model="${['colors':boardInstance.colors.sort{a,b -> a.name <=> b.name}]}"></g:render>
   		</content>    	    			  
    </body>
</html>
