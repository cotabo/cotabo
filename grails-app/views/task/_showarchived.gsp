<g:if test="${taskInstance.archived}" >
<g:set var="lastColumn" value="${taskInstance.column.board.columns.last()}"/>
<li id="task_${taskInstance.id}" class="task">	
	<div class="task-header">	
		<img class="pull-left avatar" 
			src="${taskInstance.assignee?.avatar ? createLink(controller:'user', action:'avatar', id:taskInstance?.assignee?.username) : resource(dir:'images/icons',file:'user_12x16.png')}" 
			title="${taskInstance.assignee ? taskInstance?.assignee?.firstname + ' ' + taskInstance?.assignee?.lastname : 'no assignee' }"></img>		
		<b>#${taskInstance.id} - ${taskInstance?.name?.encodeAsHTML()}</b>
		<g:if test="${taskInstance.column.id == lastColumn.id}">			
			<img src="${resource(dir:'images/icons',file:'document_stroke_12x12.png')}" class="icon pull-right archive"></img>			
		</g:if>
		<img class="expander pull-right icon" src="${resource(dir:'images/icons',file:'arrow_down_12x12.png')}" title="expand / collapse"></img>
		<g:each in="${taskInstance.colors.sort{a, b -> a.name <=> b.name}}" var="color">
			<div class="head_color pull-right icon ${color?.name?.encodeAsHTML()}" style="background-color:${color?.color?.encodeAsHTML()};" title="${color?.name?.encodeAsHTML()}"></div>
		</g:each>
	</div>
	<div class="task-content" style="display:none;">
		<table class="bordered-table zebra-striped">
			<tbody>
				<tr>
					<td><b>Description:</b></td>
					<td id="task_${taskInstance.id}_description">${taskInstance?.description?.encodeAsHTML() ?: '&nbsp;'}</td>
				</tr>
				<tr>
					<td><b>Details:</b></td>
					<td id="task_${taskInstance.id}_details">${taskInstance?.details?.encodeAsHTML()?.replaceAll("\n", "<br/>") ?: '&nbsp;'}</td>
				</tr>
				<tr>
					<td><b>Priority:</b></td>
					<td id="task_${taskInstance.id}_priority">${taskInstance?.priority?.encodeAsHTML()}</td>
				</tr>
				<tr>
					<td><b>Stats:</b></td>
					<td>
						<g:if test="${taskInstance.dateCreated}">
						created: <prettytime:display date="${taskInstance.dateCreated}" /><br/>
						</g:if>
						<g:if test="${taskInstance.workflowStartDate}">
						started: <prettytime:display date="${taskInstance.workflowStartDate}" /><br/>
						</g:if>
						<g:if test="${taskInstance.workflowEndDate}">
						ended: <prettytime:display date="${taskInstance.workflowEndDate}" />
						</g:if>
												
					</td>
				</tr>
			</tbody>
		</table>
	</div>
</li>	
</g:if>
<g:else>
<li id="task_${taskInstance.id}" class="task invisible">
</li>
</g:else>
