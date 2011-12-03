<g:form controller="taskColor" action="save" name="taskColorCreateForm">
	<fieldset>
		<div class="clearfix">
			<label for="name">label *</label>
			<div class="input">
				<g:textField name="name" maxlength="25" value=""/>
			</div>
		</div>
		<div class="clearfix">
			<label for="description">color</label>
			<div class="input">
				 <g:textField name="color" maxlength="7" value="" placeholder="#FF0000"/>
			</div>
		</div>
		<div class="clearfix">
			<div class="input">
				<g:actionSubmit value="Create Tag" class="btn primary"/>
			</div>
		</div>
	</fieldset>
	<g:hiddenField name="board" value="${boardInstance.id}"/>
</g:form>

<jq:jquery>
	$('#color_create_dialog').bind('shown', function() {	
		$('input[name="name"]').focus();
	});
	/**
	 * Submit event overwrite for the task create form.
	 */	
	$('#taskColorCreateForm').submit(function() {		
		$.ajax({		
			type: 'POST',
			url: $(this).attr('action'),
			data: $(this).serialize()			
		 });	
		 $('#color_create_dialog').modal('hide');				 
		 //Don't really submit
		 return false;
	});	
</jq:jquery>