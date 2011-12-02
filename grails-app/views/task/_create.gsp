<%@ page import="org.cotabo.RoleEnum" %>
<g:render template="/info" model="[messagecode: 'form.required.fields']"/>  
<g:form controller="task" action="save" name="taskForm">
	<fieldset>
		<div class="clearfix">
			<label for="name" alt="A short task name">name *</label>
			<div class="input">
				<g:textField name="name" maxlength="25"/>
			</div>
		</div>
		<div class="clearfix">
			<label for="description" alt="A description of the Task" class="optional">description</label>
			<div class="input">
				<g:textArea class="xlarge" name="description" cols="30" rows="3" maxlength="254"/>
			</div>
		</div>
		<div class="clearfix">
			<label for="details" alt="Details of the Task" class="optional">details</label>
			<div class="input">
				<g:textArea class="xlarge" name="details" cols="30" rows="3" maxlength="254"/>					
			</div>
		</div>		
		<div class="clearfix">
			<label for="assignee" alt="The assignee for this task" class="optional">assignee</label>
			<div class="input">
	 					<g:set var="assignees" value="${(boardInstance.getUsers(RoleEnum.USER) +  boardInstance.getUsers(RoleEnum.ADMIN)).unique()}"/>
	 					<g:select name="assignee" 
	 						from="${assignees}" 
	 						noSelection="${['':'']}"
	 						value=""
	 						optionKey="id"
				/>			
			</div>
		</div>
		<div class="clearfix">
			<label for="priority" alt="The priority of this task">priority *</label>
			<div class="input">
				<g:select from="${priorities}" value="${grailsApplication.config.taskboard.default.priority}" name="priority"></g:select>					
			</div>
		</div>
		<div class="clearfix">
			<label for="color" alt="The color with this this task gets displayed">color *</label>
			<div class="input">
				<g:select id="update_color" from="${boardInstance.colors?.sort{a,b -> a.name <=> b.name}}" optionKey="color" optionValue="name" name="color" multiple="true" size="4"></g:select>							
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
$('select[name="color"]').each(function() {
	$(this).attr("style", "background-color:"+this.value);
});
   
$('select[name="color"]').change(function(event) {
   $('select[name="color"]').attr("style", $('#color > option:selected').attr("style"));
}).change();

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
 