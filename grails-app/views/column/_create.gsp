<div id="columns_content" >	
   	<table>
   		<tbody>
   			<tr>		     					
   				<th>Name</th>
 				<th>Short description</th>
 				<th>Task limit</th>
 				<th>&nbsp;</th>
   			</tr>
   			<g:each in="${boardInstance.columns}" var="column" status="i">
  			<tr class="column">
  				<td>
  					<g:textField name="columns[$i].name" maxlength="75" value="${boardInstance?.columns[i]?.name}" />
  				</td>
  				<td>
  					<g:textField name="columns[$i].description" size="50" value="${boardInstance?.columns[i]?.description}" />
  				</td>
  				<td>
  					<g:textField name="columns[$i].limit" size="2" maxlength="2" value="${boardInstance?.columns[i]?.limit}" />
  				</td>
  				<td>
  					<a href="#" class="delete">&nbsp;</a>	
  				</td>
  			</tr>	     					
   			</g:each> 
   		</tbody>
   	</table>				     					
	<button class="add" type="button">add column</button>		
</div>    