<h1>create new column</h1>
<g:form action="save" controller="column">
	<div class="dialog">
		<table>
			<tbody>
				<tr class="prop">
					<td valign="top" class="name">
						<label for="name">Name</label>
					</td>
					<td valign="top" class="value">
						<g:textField name="name" maxlength="25">
					</td>					
				</tr>
				<tr class="prop">
					<td valign="top" class="name">
						<label for="description">Description</label>
					</td>
					<td valign="top" class="value">
						<g:textArea name="description" cols="40" rows="4">
					</td>					
				</tr>
				<tr class="prop">
					<td valign="top" class="name">
						<label for="limit">Task limit</label>
					</td>
					<td valign="top" class="value">
						<g:textField name="name" size="2" maxlength="2">
					</td>					
				</tr>
			</tbody>
		</table>
	</div>
</g:form>