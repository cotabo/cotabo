<g:set var="lastColumn" value="${taskInstance.column.board.columns.last()}"/>
				
<li id="task_${taskInstance.id}" class="task">	
	<div class="task-header">	
		<img class="pull-left avatar" src="${taskInstance?.assignee?.avatar ? createLink(controller:'user', action:'avatar', id:taskInstance?.assignee?.username) : resource(dir:'images/icons/dark_grey',file:'user_12x16.png')}" title="${taskInstance?.assignee}"></img>		
		<b>#${taskInstance.id} - ${taskInstance?.name?.encodeAsHTML()}</b>
		<g:if test="${taskInstance.column.id == lastColumn.id}">
			<a href="${createLink(controller:'task', action:'archive', id:taskInstance?.id)}" class="icon pull-right" title="archive">
				<img src="${resource(dir:'images/icons/dark_grey',file:'document_stroke_12x12.png')}"></img>
			</a>
		</g:if>
		<img class="block-box pull-right icon ${taskInstance?.blocked ? '' : 'greyed-out'}" src="${resource(dir:'images/icons/dark_grey',file:'lock_stroke_9x12.png')}" title="block / unblock"></img>
		<img class="expander pull-right icon" src="${resource(dir:'images/icons/dark_grey',file:'arrow_down_12x12.png')}" title="expand / collapse"></img>
		<g:each in="${taskInstance.colors}" var="color">
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
					<td><b>Assignee:</b></td>
					<td id="task_${taskInstance.id}_assignee">
						${taskInstance?.assignee?.username?.encodeAsHTML() ?: '&nbsp;'}						
					</td>
				</tr>
			</tbody>
		</table>
	</div>
</li>			