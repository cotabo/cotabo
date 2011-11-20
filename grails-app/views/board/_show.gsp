<div id="board">	
	<g:each in="${boardInstance.columns}" var="columnInstance">
	<g:render template="/column/show" model="['columnInstance':columnInstance]"/>
	</g:each>
</div>