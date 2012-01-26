<g:if test="${taskInstance}">
<g:set var="lastColumn" value="${taskInstance.column.board.columns.last()}"/>
<g:if test="${!taskInstance.archived}">			
<li id="task_${taskInstance.id}" class="task">	
	<div class="task-header">	
		<img class="pull-left avatar" 
			src="${taskInstance.assignee?.avatar ? createLink(controller:'user', action:'avatar', id:taskInstance?.assignee?.username) : resource(dir:'images/icons',file:'user_12x16.png')}" 
			title="${taskInstance.assignee ? taskInstance?.assignee?.firstname + ' ' + taskInstance?.assignee?.lastname : 'no assignee' }"></img>		
		<b>#${taskInstance.id} - ${taskInstance?.name?.encodeAsHTML()}</b>
		<g:if test="${taskInstance.column.id == lastColumn.id}">			
		<img src="${resource(dir:'images/icons',file:'document_stroke_12x12.png')}" class="icon pull-right archive"></img>			
		</g:if>
		<img class="block-box pull-right icon ${taskInstance?.blocked ? '' : 'greyed-out'}" src="${resource(dir:'images/icons',file:'lock_stroke_9x12.png')}" title="block / unblock"></img>
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
						<g:if test="${taskInstance.due}">
						due: <prettytime:display date="${taskInstance.due}" /><br/>
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
	<g:if test="${taskInstance.deadline }">
		<jq:jquery>
			$("#task_${taskInstance.id}")[0].style.border = '2px solid red';
			/*localStorage.setItem(${taskInstance.id}, self.setInterval(function() {
			  $("#task_${taskInstance.id}").effect("bounce", {times: 5}, 500);}, 
			  2000));
			  console.log("setItem: " + ${taskInstance.id});*/
		</jq:jquery>
	</g:if>
	<g:else>
		<jq:jquery>
			$("#task_${taskInstance.id}")[0].style.border = '';
			/*if(localStorage.getItem(${taskInstance.id})){
				window.clearInterval(localStorage.getItem(${taskInstance.id}));
				localStorage.removeItem(${taskInstance.id})
				console.log("removeItem: " + ${taskInstance.id});
			}*/
		</jq:jquery>
	</g:else>
</li>	
</g:if>
</g:if>