<div id="board">	
	<g:set var="columns" value="${boardInstance.archivedcolumns }"/>
	<g:set var="columnsSize" value="${columns.size() }" />
	<g:each in="${columns}" var="columnInstance">
		<g:render template="/column/showarchived" model="['columnInstance':columnInstance, 'columnsSize':columnsSize]"/>
	</g:each>
</div>