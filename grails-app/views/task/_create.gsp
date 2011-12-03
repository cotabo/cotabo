<%@ page import="org.cotabo.RoleEnum" %>
<g:render template="/info" model="[messagecode: 'form.required.fields']"/>  
<g:form controller="task" action="save" name="taskForm">
	<fieldset>
		<div class="clearfix">
			<label for="name">name *</label>
			<div class="input">
				<g:textField name="name" maxlength="25"/>
			</div>
		</div>
		<div class="clearfix">
			<label for="description">description</label>
			<div class="input">
				<g:textArea class="xlarge" name="description" cols="30" rows="3" maxlength="254"/>
			</div>
		</div>
		<div class="clearfix">
			<label for="details">details</label>
			<div class="input">
				<g:textArea class="xlarge" name="details" cols="30" rows="3" maxlength="254"/>					
			</div>
		</div>		
		<div class="clearfix">
			<label for="assignee">assignee</label>
			<div class="input">
				<g:set var="assignees" value="${(boardInstance.getUsers(RoleEnum.USER) +  boardInstance.getUsers(RoleEnum.ADMIN)).unique()}"/>
				<g:select name="assignee" 
					from="${assignees}" 
					noSelection="${['':'']}"
					value=""
					optionKey="id"/>			
			</div>
		</div>
		<div class="clearfix">
			<label for="priority">priority *</label>
			<div class="input">
				<g:select from="${priorities}" 
					value="${grailsApplication.config.taskboard.default.priority}" 
					name="priority"/>					
			</div>
		</div>
		<div class="clearfix">
			<label for="color">color *</label>
			<div class="input">
				<g:select from="${boardInstance.colors.sort{a,b -> a.name <=> b.name}}" 
					class="medium" 
					optionKey="color"
					optionValue="name" 
					name="color" 
					multiple="true" 
					size="4"/>							
			</div>
		</div>
		<div class="clearfix">
			<div class="input">
				<g:actionSubmit value="Create Task" class="btn primary"/>
			</div>
		</div>
		<g:hiddenField name="board" value="${boardInstance.id}"/>
	</fieldset>    	
 </g:form>
 <jq:jquery>
$('#task_create_dialog').bind('shown', function() {	
	$('input[name="name"]').focus();
});
$('#color > option').each(function() {
	$(this).attr("style", "background-color:"+this.value);
});   
$('form#taskForm').submit(function(e) { 	
	$.ajax({		
		type: 'POST',
		url: $(this).attr('action'),
		data: $(this).serialize()			
	});	
	$('#task_create_dialog').modal('hide');				 
	//Don't really submit
	return false;	
 });
 </jq:jquery>
 