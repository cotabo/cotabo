<tb:board column="${boardInstance}">
	<g:each in="${boardInstance.columns}" var="columnInstance">
	<g:render template="/column/show" model="[columnInstance:$columnInstance]"/>
	</g:each>
</tb:column>