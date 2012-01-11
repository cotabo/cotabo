<g:set var="width" value="${(100 / nColumns) -0.1}"/>
<div id="column_${columnInstance.id}" class="column pull-left" style="width:${width}%;">
	<span class="title">
		<h4>${columnInstance.name.encodeAsHTML()}</h4>
		<span>
			<h4 class="value">${columnInstance.tasks?.size()?: 0}</h4>
			<g:if test="${ columnInstance.limit > 0}">
			<h4>/</h4>
			<h4 class="limit">${columnInstance.limit?.toString().encodeAsHTML()}</h4>
			</g:if>			
		</span>
	</span>
	<ul>
		<g:each in="${columnInstance.tasks}" var="taskInstance">
			<g:if test="${archivedView }">
				<g:render template="/task/showarchived" model="['taskInstance':taskInstance, 'archivedView': archivedView]"/>
			</g:if>
			<g:else>
				<g:render template="/task/show" model="['taskInstance':taskInstance, 'archivedView': archivedView]"/>
			</g:else>
		</g:each>
	</ul>
</div>
