<div class="alert-message block-message">	
	<span>
		<g:if test="${messagecode != null}">
			<b><g:message code="${messagecode}"/></b>
		</g:if>
		<g:else>
			<b>${message}</b>
		</g:else>
	</span>
</div>
