<%
	/*
	Helper template to display modal dialogs
	The following model data should be provided:
		id - the ID of the dialog
		header - [optional] a text to be displayed in the header
		body - a text to be displayed in the body
		primary - [optional] a text for the first button. 
					The link behing is will become the href target of the last clicked element with the data-controls-modal attribute defined.
		secondary - [optiponal] a text for a secondary button - a click will just close the dialog.
	*/
 %>
<div id="${id}" class="modal hide fade">
	<g:if test="${header}">
	<div class="modal-header">
	    <a href="#" class="close">&times;</a>
    	<h3>${header}</h3>
	</div>
	</g:if>
    <div class="modal-body">
    	<p>${body}</p>
    </div>
    <g:if test="${primary || secondary}">
    <div class="modal-footer">
    	<g:if test="${primary}"><a href="#" class="btn primary">${primary}</a></g:if>
  		<g:if test="${secondary}"><a href="#" class="btn secondary">${secondary}</a></g:if>
    </div>    
    </g:if>
</div>
<jq:jquery>
	<g:if test="${sourceSelector}">
	$('${sourceSelector}').click(function() {
	</g:if>
	<g:else>
	$('a[data-controls-modal]').click(function() {
	</g:else>			
		document.modalHelperHref = $(this).attr('href');
	});
	<g:if test="${secondary}">
	$('#${id} > .modal-footer > a.secondary').click(function() {
		$('#${id}').modal('hide');
	});		
	</g:if>
	
	$('#${id}').bind('show', function(e) {
		$('#${id} > .modal-footer > a.primary').attr('href', document.modalHelperHref)		
	});
		
	$('#${id}').bind('hide', function(e) {
		//reset the href
		$('#${id} > .modal-footer > a:first').attr('href', '#');		
	});
	
	
</jq:jquery>