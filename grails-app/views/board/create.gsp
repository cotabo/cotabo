<%@ page import="org.cotabo.Board" %>
<%@ page import="org.cotabo.RoleEnum" %>
<g:if test="${edit}">
    <g:set var="title" value="Cotabo - update board (${boardInstance.name})"/>
    <g:set var="h1" value="update '${boardInstance.name}'"/>
    <g:set var="button" value="update board"/> 
    <g:set var="id" value="${boardInstance.id}"/>
</g:if>
<g:else>
    <g:set var="title" value="Cotabo - create new board"/>
    <g:set var="h1" value="new board"/>
    <g:set var="button" value="create board"/>
    <g:set var="id" value=""/>
</g:else>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />        
        <link rel="stylesheet" href="${resource(dir:'css',file:'board_create.css')}" />
        <link rel="stylesheet" href="${resource(dir:'css',file:'jquery-ui-1.8.12.custom.css')}" />        
        <script type="text/javascript" src="${resource(dir:'js/jquery-ui', file:'jquery-ui-1.8.6.custom.min.js')}"></script>
        <script type="text/javascript" src="${resource(dir:'js/jquery-plugins', file:'jquery.flydom-3.1.1.js')}"></script>        
        <script type="text/javascript" src="${resource(dir:'js', file:'board_create.js')}"></script>        
        <title>${title}</title>
    </head>
    <body>	
    	<content tag="herounit.hook">
    		<div class="hero-unit">
    			<h3>${h1}</h3>
    		</div>
    	</content>	   
	    <g:if test="${flash.message}">
	    <div class="message">${flash.message}</div>
	    </g:if>
	    <g:hasErrors bean="${boardInstance}">
	    <div class="ui-state-error ui-corner-all">
	        <g:renderErrors bean="${boardInstance}" as="list" />
	    </div>
	    </g:hasErrors>
	    

	    <g:form action="save" id="${id}">	
	    	<fieldset>
	    		<legend>Board</legend>
   				<div class="clearfix">
					<label for="name">name</label>
					<div class="input">
						 <g:textField name="name" maxlength="25" value="${boardInstance?.name}" />
					</div>
				</div>
   				<div class="clearfix">
					<label for="name">description</label>
					<div class="input">
						 <g:textArea name="description" class="xlarge" cols="40" rows="5" value="${boardInstance?.description}" />
					</div>
				</div>				

				<legend>User</legend>
				<br/>
				<div id="usermanagement clearfix">
					<label>available user</label>
				    <div id="users_selectable" class="um_div">        
				        <ul id="list_selectable" class="user_list input">
				            <g:each in="${allUsers}" var="user" status="i">            
				            <li class="user_item pull-left">
				                <div class="div_user">
				                    ${user}
				                    <g:hiddenField name="user[${i}]" value="${user.id}"/>
				                </div>
				            </li>
				            </g:each>
				            
				        </ul>
				    </div>
				    <div id="users_selected" class="um_div">
				        <g:each in="${RoleEnum.values()}" var="role">            	       
					        <div class="clearfix">
					           <label><tb:printRole role="${role}"/></label>
					           <ul id="${role}" class="user_droppable user_list input" 
					               title='<g:message code="user.role.${role}" default=""/>'>
					               <g:if test="${edit}">
					               <g:each in="${boardInstance.getUsers(role)}" var="user" status="i">
					               <li class="user_item pull-left">
					                   <div class="div_user">
					                   ${user}
					                   <g:hiddenField name="${role}" value="${user.id}"/>
					                   </div>
					               </li>
					               </g:each>
					               </g:if>
					           </ul>       
					        </div>
				        </g:each>
				    </div>				    				   
				</div>

				<legend>Columns</legend>
				<br/>
				<div class="clearfix">
					<div class="row">
						<div class="span4">name</div>
						<div class="span5">short description</div>
						<div class="span2">task limit</div>
						<div class="span2">workflow start</div>
						<div class="span2">workflow end</div>
						<div class="span1">&nbsp;</div>
					</div>
				</div>
				<div id="columns">
					<g:each in="${boardInstance.columns}" var="column" status="i">
					<div class="clearfix">
						<div class="row">
							<div class="span4">
								<g:textField name="columns[$i].name" maxlength="75" class="span4" value="${boardInstance?.columns[i]?.name}" />
							</div>
							<div class="span5">
								<g:textField name="columns[$i].description" class="span5" value="${boardInstance?.columns[i]?.description}" />
							</div>
							<div class="span2">
								<g:textField name="columns[$i].limit" class="span2" maxlength="2" value="${boardInstance?.columns[i]?.limit}" />
							</div>
							<div class="span2">
							    <g:if test="${edit}">					        
							    	<g:set var="checked" value="${boardInstance?.columns[i]?.workflowStartColumn}"/>
								</g:if>
								<g:else>
								    <g:set var="checked" value="${i == 0 ? true : false }"/>
								</g:else>
								<g:radio name="workflowStart" checked="${checked}" value="$i"/>
							</div>
							<div class="span2">
							    <g:if test="${edit}">					        
							    	<g:set var="checked" value="${boardInstance?.columns[i]?.workflowEndColumn}"/>
								</g:if>
								<g:else>
								    <g:set var="checked" value="${i == (boardInstance?.columns?.size()-1) ? true : false }"/>
								</g:else>
								<g:radio name="workflowEnd" checked="${checked}" value="$i"/>
							</div>
							<div class="span1">
								<img class="icon delete" title="delete this column" src="${resource(dir:'images/icons',file:'trash_stroke_12x12.png')}"/>							
							</div>
						</div>
					</div>
					</g:each>
				</div>
				<div class="btn add_column">add column</div>
				<div class="actions">
					<button type="submit" class="btn primary">${button}</button>
				</div>
	    	</fieldset>   
		</g:form>	
    </body>
</html>
