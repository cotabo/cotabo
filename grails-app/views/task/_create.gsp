<div id="createTaskForm" title="new task">	
    <g:if test="${flash.message}">
    <div class="message">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${boardInstance}">
	    <div class="errors">
	    <g:renderErrors bean="${boardInstance}" as="list" />
	    </div>
    </g:hasErrors>
    <g:form controller="task" action="save">
    <div>
    	<table>
    		<tbody>
    			<tr>
    				<td>
    					<label for="name" alt="A short task name">name</label>
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
    					<g:textArea name="description" cols="30" rows="3"/>
    				</td>
    			</tr>
    			<tr>
    				<td>
    					<label for="duration" alt="The duration of the task (in hours)" class="optional">duration (h)</label>
    				</td>
    				<td>
    					<g:textField name="name" size="3"/>
    				</td>
    			</tr>
    			<tr>
    				<td>
    					<label for="Priority" alt="The priority of this task"></label>
    				</td>
    				<td>
    					
    				</td>
    			</tr>
    		</tbody>
    	</table>
    </div>
    </g:form>
</div>