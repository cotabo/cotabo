<%@ page import="org.cotabo.RoleEnum" %> 
<tb:modal id="task_update_dialog" header="Update Task">    
<g:render template="/info" model="[messagecode: 'form.required.fields']"/> 
<g:form controller="task" action="update" name="taskUpdateForm" id="${taskInstance.id}">
	<fieldset>
		<div class="clearfix">
			<label for="name">name *</label>
			<div class="input">
				<g:textField name="name" maxlength="25" value="${taskInstance.name}"></g:textField>
			</div>
		</div>
		<div class="clearfix">
			<label for="description">description</label>
			<div class="input">
				<g:textArea class="xlarge" name="description" cols="30" rows="3" maxlength="254">${taskInstance.description}</g:textArea>				
			</div>
		</div>
		<div class="clearfix">
			<label for="details">details</label>
			<div class="input">				
				<g:textArea class="xlarge" name="details" cols="30" rows="3" maxlength="254">${taskInstance.details}</g:textArea>					
			</div>
		</div>	
		<div class="clearfix">
			<label for="assignee">assignee</label>
			<div class="input">
	            <g:set var="assignees" value="${(taskInstance.column.board.getUsers(RoleEnum.USER) +  taskInstance.column.board.getUsers(RoleEnum.ADMIN)).unique()}"/>
	            <g:select name="assignee" 
	                from="${assignees}"                             
	                value="${taskInstance.assignee?.id}"
	                optionKey="id"/>
			</div>
		</div>
		<div class="clearfix">
			<label for="priority">priority *</label>
			<div class="input">
				<g:select from="${grailsApplication.config.taskboard.priorities}" value="${taskInstance.priority}" name="priority"/>					
			</div>
		</div>
		<div class="clearfix">
			<label for="color">color *</label>
			<div class="input">
				<g:select id="update_color" 
					from="${taskInstance.column.board.colors.sort{a,b -> a.name <=> b.name}}" 
					value="${taskInstance.colors}" 
					optionKey="color" 
					optionValue="name" 
					name="color" 
					multiple="true" 
					size="4"/>							
			</div>
		</div>
		<div class="clearfix">
			<div class="input">
				<g:actionSubmit value="Update Task" class="btn primary"/>
			</div>
		</div>
	</fieldset>       
</g:form>
<jq:jquery>
$('#task_update_dialog').bind('shown', function() {	
	$('input[name="name"]').focus();
});

$('#update_color > option').each(function() {
	$(this).attr("style", "background-color:"+this.value);
});

$('#taskUpdateForm').submit(function() {      
    $.ajax({        
        type: 'POST',
        url: $(this).attr('action'),
        data: $(this).serialize()           
     });    
     $('#task_update_dialog').modal('hide'); 	     
     //avoid the event bubbeling for this submit
     return false;
});
</jq:jquery>
</tb:modal>