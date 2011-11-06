<tb:board board="${boardInstance}">
	<g:each in="${boardInstance.columns}" var="columnInstance">
	<g:render template="/column/show" model="['columnInstance':columnInstance]"/>
	</g:each>
</tb:board>