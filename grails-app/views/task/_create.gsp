<jq:jquery>    
    /**
     * Dialog with form content.
     */
    $('#createTaskForm').dialog({
        autoOpen: false,
        height: 480,
        width: 400,
        modal: true,
        buttons: {
            "create task": function() {                     
                $('#taskForm').submit();                
            },          
            "cancel": function() {
                $( this ).dialog( "close" );
            }
        },
				close: function() {
					 $("input").blur();
				}
    });
    
	/**
	 * Submit event overwrite for the task create form.
	 */	
	$('#taskForm').submit(function() {		
		$.ajax({		
			type: 'POST',
			url: $(this).attr('action'),
			data: $(this).serialize()			
		 });	
		 $('#createTaskForm').dialog('close');				 
		 //Don't really submit
		 return false;
	});
	
	$('#color > option').each(function() {
		$(this).attr("style", "background-color:"+this.value);
	});
    
	$('select[name="color"]').change(function(event) {
	   $('select[name="color"]').attr("style", $('#color > option:selected').attr("style"));
	}).change();
	
</jq:jquery>
<%@ page import="org.cotabo.RoleEnum" %>
<div id="createTaskForm" title="new task">	
    <g:hasErrors bean="${boardInstance}">
	    <div class="errors">
	    <g:renderErrors bean="${boardInstance}" as="list" />
	    </div>
    </g:hasErrors>
    <g:render template="/info" model="[messagecode: 'form.required.fields']"/>  
    <g:form controller="task" action="save" name="taskForm">
    <div>
    	<table>
    		<tbody>
    			<tr>
    				<td>
    					<label for="name" alt="A short task name">name *</label>
    				</td>
    				<td>
    					<g:textField name="name" maxlength="25"/>
    				</td>
    			</tr>
    			<tr>
    				<td>
    					<label for="description" alt="A description of the Task" class="optional">description</label>
    				</td>
    				<td>
    					<g:textArea name="description" cols="30" rows="3" maxlength="254"/>
    				</td>
    			</tr>
    			<tr>
    				<td>
    					<label for="details" alt="Details of the Task" class="optional">details</label>
    				</td>
    				<td>
    					<g:textArea name="details" cols="30" rows="3" maxlength="254"/>
    				</td>
    			</tr>
 			    <tr>
    				<td>
    					<label for="assignee" alt="The assignee for this task" class="optional">assignee</label>
    				</td>
    				<td>
    					<g:set var="assignees" value="${(boardInstance.getUsers(RoleEnum.USER) +  boardInstance.getUsers(RoleEnum.ADMIN)).unique()}"/>
    					<g:select name="assignee" 
    						from="${assignees}" 
    						noSelection="${['':'']}"
    						value=""
    						optionKey="id"
						/>
    				</td>
    			</tr>
    			<tr>
    				<td>
    					<label for="priority" alt="The priority of this task">priority *</label>
    				</td>
    				<td>
    					<g:select from="${priorities}" value="${grailsApplication.config.taskboard.default.priority}" name="priority"></g:select>
    				</td>
    			</tr>
    			<tr>
    				<td>
    					<label for="color" alt="The color with this this task gets displayed">color *</label>
    				</td>
    				<td>
    					<g:select from="${boardInstance.colors.sort{a,b -> a.name <=> b.name}}" optionKey="color" optionValue="name" name="color" multiple="true" size="4"></g:select>
    				</td>
    			</tr>
    		</tbody>
    	</table>
    </div>    
    <g:hiddenField name="board" value="${boardInstance.id}"/>
    </g:form>
</div>