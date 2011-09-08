<div id="columns_content" >	
   	<table>
   	    <colgroup>
	        <col width="25%"/>
	        <col width="40%"/>
	        <col width="15%"/>
	        <col width="8%"/>
	        <col width="8%"/>
	        <col width="4%"/>
   	    </colgroup>
   		<tbody>   		
   			<tr>		     					
   				<th>Name</th>
 				<th>Short description</th>
 				<th>Task limit</th>
 				<th>Workflow start</th>
 				<th>Workflow end</th>
 				<th>&nbsp;</th>
   			</tr>
 		</tbody>
 	</table>
 	<ul class="column_list">
	<g:each in="${boardInstance.columns}" var="column" status="i">
	   <li>
		<table>
	        <colgroup>
	            <col width="25%"/>
	            <col width="40%"/>
	            <col width="15%"/>
	            <col width="8%"/>
	            <col width="8%"/>
	            <col width="4%"/>
	        </colgroup>
		    <tbody>
				<tr class="column">
					<td>
						<g:textField name="columns[$i].name" maxlength="75" size="40" value="${boardInstance?.columns[i]?.name}" />
					</td>
					<td>
						<g:textField name="columns[$i].description" size="60" value="${boardInstance?.columns[i]?.description}" />
					</td>
					<td>
						<g:textField name="columns[$i].limit" size="2" maxlength="2" value="${boardInstance?.columns[i]?.limit}" />
					</td>
					<td>
					    <g:if test="${edit}">					        
						    <g:set var="checked" value="${boardInstance?.columns[i]?.workflowStartColumn}"/>
						</g:if>
						<g:else>
						    <g:set var="checked" value="${i == 0 ? true : false }"/>
						</g:else>
						<g:radio name="workflowStart" checked="${checked}" value="$i"/>
					</td>
					<td>
					    <g:if test="${edit}">					        
						    <g:set var="checked" value="${boardInstance?.columns[i]?.workflowEndColumn}"/>
						</g:if>
						<g:else>
						    <g:set var="checked" value="${i == (boardInstance?.columns?.size()-1) ? true : false }"/>
						</g:else>
						<g:radio name="workflowEnd" checked="${checked}" value="$i"/>
					</td>
					<td>
						<a href="#" class="delete">delete this column definition</a>	
					</td>
				</tr>
			<tbody>	
		</table>  
	   </li>   					
	</g:each> 
	</ul>	     					
	<button class="add" type="button">add column</button>		
</div>    