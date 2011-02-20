<div class="ui-state-highlight ui-corner-all" style="margin-top: 10px; margin-bottom: 10px;padding: 0pt 0.7em; padding:5px;">
	<span class="ui-icon ui-icon-info" style="float:left; margin-right: 0.3em;"></span>
	<span>
		<g:if test="${messagecode != null}">
			<g:message code="${messagecode}"/>
		</g:if>
		<g:else>
			${message}
		</g:else>
	</span>
</div>
