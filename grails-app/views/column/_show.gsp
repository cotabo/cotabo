<tb:column column="${columnInstance}">
	<g:each in="${columnInstance.tasks}" var="taskInstance">
	<g:render template="/task/show" model="[taskInstance:$taskInstance]"/>
	</g:each>
</tb:column>
