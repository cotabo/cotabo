<g:each in="${colors}" var="color">
	<div style="width:${100/colors.size()}%">
		<div class="column" style="background-color:${color}; min-height:0px;margin:3px;padding:3px;font-size:14px; ">
			<%--<g:form action="delete" controller="taskColor"> 
				<input type="hidden" name="id" value="${color.id }" />
				<input type="submit" class="ui-icon ui-icon-close tag" />
				${color.name }
			</g:form>
			 --%>
			 
			<a href="${createLink(controller:'taskColor', action:'delete', id:color.id)}"><span class="ui-icon ui-icon-close tag"></span></a>
			${color.name }
		</div>
	</div>
</g:each>