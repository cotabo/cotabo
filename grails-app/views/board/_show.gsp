<div id="board">	
	<g:set var="columns" value="${fake? boardInstance.archivedcolumns : boardInstance.columns}"/>
	<g:set var="ncolumns"  value="${columns.size() }"/>
	<g:set var="archivedView" value="${fake }" />
	<g:each in="${columns}" var="columnInstance">
		<g:render template="/column/show" model="['columnInstance':columnInstance, 'nColumns':ncolumns, 'archivedView':archivedView ]"/>
	</g:each>
</div>