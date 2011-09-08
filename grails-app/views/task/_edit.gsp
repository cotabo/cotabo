<%@ page import="org.cotabo.RoleEnum" %>
<div id="taskUpdateDialog" title="update task">
	<jq:jquery>
	    /**
	     * Update dialog with form content.
	     */
	    $('#taskUpdateDialog').dialog({
	        autoOpen: true,
	        height: 400,
	        width: 400,
	        modal: true,
	        buttons: {
	            "update task": function() {                     
	                $('#taskUpdateForm').submit();                
	            },          
	            "cancel": function() {
	                $( this ).dialog( "close" );
	            }
	        },
	        close: function() {
	           //removing ourself from the DOM on close
	           $('div#taskUpdateDialog').remove();
	           $("input").blur();
	        }
	    });
	    
	    /**
	     * Submit event overwrite for the task update form.
	     */ 
	    $('#taskUpdateForm').submit(function() {      
	        $.ajax({        
	            type: 'POST',
	            url: $(this).attr('action'),
	            data: $(this).serialize()           
	         });    
	         $('#taskUpdateDialog').dialog('close');               
	         //avoid the event bubbeling for this submit
	         return false;
	    });
	    
	    $('#update_color > option').each(function() {
	        $(this).attr("style", "background-color:"+this.value);
	    });
	    
       $('#update_color').change(function(event) {
	      $('#update_color').attr("style", $('#update_color > option:selected').attr("style"));
	   }).change();
	    
	</jq:jquery>     
    <g:render template="/info" model="[messagecode: 'form.required.fields']"/> 
    <g:form controller="task" action="update" name="taskUpdateForm" id="${taskInstance.id}">
    <div>
        <table>
            <tbody>
                <tr>
                    <td>
                        <label for="name" alt="A short task name">name *</label>
                    </td>
                    <td>                        
                        <g:textField name="name" maxlength="25" value="${taskInstance.name}"></g:textField>
                    </td>
                </tr>
                <tr>
                    <td>
                        <label for="description" alt="A description of the Task" class="optional">description</label>
                    </td>
                    <td>
                        <g:textArea name="description" cols="30" rows="3" maxlength="254">${taskInstance.description}</g:textArea>
                    </td>
                </tr>
                <tr>
                    <td>
                        <label for="details" alt="Details of the Task" class="optional">details</label>
                    </td>
                    <td>
                        <g:textArea name="details" cols="30" rows="3" maxlength="254">${taskInstance.details}</g:textArea>
                    </td>
                </tr>
                <tr>
                    <td>
                        <label for="assignee" alt="The assignee for this task" class="optional">assignee</label>
                    </td>
                    <td>
                        <g:set var="assignees" value="${(taskInstance.column.board.getUsers(RoleEnum.USER) +  taskInstance.column.board.getUsers(RoleEnum.ADMIN)).unique()}"/>
                        <g:select name="assignee" 
                            from="${assignees}"                             
                            value="${taskInstance.assignee.id}"
                            optionKey="id"
                        />
                    </td>
                </tr>
                <tr>
                    <td>
                        <label for="priority" alt="The priority of this task">priority *</label>
                    </td>
                    <td>
                        <g:select from="${grailsApplication.config.taskboard.priorities}" value="${taskInstance.priority}" name="priority"></g:select>
                    </td>
                </tr>
                <tr>
                    <td>
                        <label for="color" alt="The color with this this task gets displayed">color *</label>
                    </td>
                    <td>
                        <g:select id="update_color" from="${grailsApplication.config.taskboard.colors}" value="${taskInstance.colors.toArray()[0].color}" name="color"></g:select>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>        
    </g:form>
</div>