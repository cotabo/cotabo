<%@ page import="org.cotabo.RoleEnum" %> 
<tb:modal id="task_update_dialog" header="Update Task">    
<g:render template="/info" model="[messagecode: 'form.required.fields']"/> 
<g:form controller="task" action="update" name="taskUpdateForm" id="${taskInstance.id}">
	<fieldset>
		<div class="clearfix">
			<label for="name" alt="A short task name">name *</label>
			<div class="input">
				<g:textField name="name" maxlength="25" value="${taskInstance.name}"></g:textField>
			</div>
		</div>
		<div class="clearfix">
			<label for="description" alt="A description of the Task" class="optional">description</label>
			<div class="input">
				<g:textArea class="xlarge" name="description" cols="30" rows="3" maxlength="254">${taskInstance.description}</g:textArea>				
			</div>
		</div>
		<div class="clearfix">
			<label for="details" alt="Details of the Task" class="optional">details</label>
			<div class="input">				
				<g:textArea class="xlarge" name="details" cols="30" rows="3" maxlength="254">${taskInstance.details}</g:textArea>					
			</div>
		</div>	
		<div class="clearfix">
			<label for="assignee" alt="The assignee for this task" class="optional">assignee</label>
			<div class="input">
	            <g:set var="assignees" value="${(taskInstance.column.board.getUsers(RoleEnum.USER) +  taskInstance.column.board.getUsers(RoleEnum.ADMIN)).unique()}"/>
	            <g:select name="assignee" 
	                from="${assignees}"                             
	                value="${taskInstance.assignee?.id}"
	                optionKey="id"
	            />
			</div>
		</div>
		<div class="clearfix">
			<label for="priority" alt="The priority of this task">priority *</label>
			<div class="input">
				<g:select from="${grailsApplication.config.taskboard.priorities}" value="${taskInstance.priority}" name="priority"></g:select>					
			</div>
		</div>
		<div class="clearfix">
			<label for="color" alt="The color with this this task gets displayed">color *</label>
			<div class="input">
				<g:select id="update_color" from="${taskInstance.column.board.colors.sort{a,b -> a.name <=> b.name}}" value="${taskInstance.colors}" optionKey="color" optionValue="name" name="color" multiple="true" size="4"></g:select>							
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
$('select[name="color"]').each(function() {
	$(this).attr("style", "background-color:"+this.value);
});   
$('select[name="color"]').change(function(event) {
   $('select[name="color"]').attr("style", $('#color > option:selected').attr("style"));
}).change();

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