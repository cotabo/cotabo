<jq:jquery>    
    /**
     * Dialog with form content.
     */
    $('#tags').dialog({
        autoOpen: false,
        height: 200,
        width: 400,
        modal: true,
        buttons: {
            "create color": function() {                     
                $('#taskColorCreateForm').submit();                
            },          
            "cancel": function() {
                $( this ).dialog( "close" );
            }
        },
				close: function() {
					 $("input").blur();
				}
    });
    
	/**
	 * Submit event overwrite for the task create form.
	 */	
	/*$('#taskColorCreateForm').submit(function() {		
		$.ajax({		
			type: 'POST',
			url: $(this).attr('action'),
			data: $(this).serialize()			
		 });	
		 $('#tags').dialog('close');				 
		 //Don't really submit
		 return false;
	});*/
	
</jq:jquery>
<div id="tags" title="new tag">
	<g:form controller="taskColor" action="save" name="taskColorCreateForm">
		<table>
			<tbody>
				<tr>
					<td>
                        <label for="name" alt="A short task color name">label *</label>
                    </td>
                    <td>                        
                        <g:textField name="name" maxlength="25" value=""></g:textField>
                    </td>
				</tr>
				<tr>
					<td>
                        <label for="color" alt="Hex color string">color *</label>
                    </td>
                    <td>                        
                        <g:textField name="color" maxlength="7" value=""></g:textField>
                    </td>
				</tr>
			</tbody>
		</table>
		<g:hiddenField name="board" value="${boardInstance.id}"/>
	</g:form>
</div>