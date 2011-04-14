<%@page import="app.taskboard.Color"%>
<jq:jquery>
	$("#color_list").selectable();
</jq:jquery>  
<ul id="color_list">
	<g:if test="${boardInstance?.colors && boardInstance.colors.size() > 0}">
	    <g:set var="collection" value="${boardInstance?.colors}"/>
	</g:if>
	<g:else>
		<g:set var="collection" value="${colors}"/>
	</g:else>	
	<g:each in="${collection}" var="color" status="i">			
		<li id="color_${color.id}" class="ui-widget-content" style="background: ${color.colorCode}">${color.colorCode}</li>
	</g:each>	
</ul>
<div style="clear:both"/>

