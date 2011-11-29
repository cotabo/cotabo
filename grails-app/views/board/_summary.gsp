<div class="span8">
	<div class="summary_header">		
		<h4><a href="${createLink(conroller:"board", action:"show", id:boardInstance.id)}">${boardInstance.name}</a></h4>
		<g:if test="${admin}">
			<span title="delete this board" class="pull-right">
				<a class="delete" href="${createLink(conroller:"board", action:"delete", id:boardInstance.id)}" 
					data-controls-modal="delete_dialog" data-backdrop="static" >
					<img class="icon" src="${resource(dir:'images/icons',file:'trash_stroke_12x12.png')}"/>
				</a>
			</span>		
			<span title="edit this board" class="pull-right">
				<a href="${createLink(conroller:"board", action:"edit", id:boardInstance.id)}">
					<img class="icon" src="${resource(dir:'images/icons',file:'pen_12x12.png')}"/>							
				</a>
			</span>
		</g:if>				
		<span title="reporting" class="pull-right">
			<a href="${createLink(conroller:"board", action:"comulativeflowchart", id:boardInstance.id)}">
				<img class="icon" src="${resource(dir:'images/icons',file:'chart_12x12.png')}"/>						
			</a>
		</span>
		<span title="number of users" class="pull-right">			
			<img class="icon pull-right" src="${resource(dir:'images/icons',file:'user_9x12.png')}"/>
			<p class="pull-right">${boardInstance.users.size()}</p>							
		</span>
	</div>
	
	<div>
		${boardInstance.description ? boardInstance.description?.encodeAsHTML(): '&nbsp;'}
		<table class="">
			<tbody>
				<tr>
					<td>
						<img class="icon" src="${resource(dir:'images/icons',file:'document_alt_fill_9x12.png')}"/>
						open tasks:
					</td>								
					<td class="highlighted_link">
						<%
							out << boardInstance.columns.collect{it == boardInstance.columns.last()? 0 : it.tasks.size()}.sum() 
						%>
					</td>
				</tr>
				<tr>
					<td>
						<img class="icon" src="${resource(dir:'images/icons',file:'check_12x10.png')}"/>
						tasks done:						
					</td>								
					<td class="highlighted_link">${boardInstance.columns.last().tasks.size()}</td>
				</tr>
				<tr>
					<td>
						<img class="icon" src="${resource(dir:'images/icons',file:'bars_12x12.png')}"/>
						overall tasks:						
					</td>
					<td class="highlighted_link"><% out << boardInstance.columns.collect{it.tasks.size()}.sum() %></td>
				</tr>
			</tbody>
		</table>
	</div>
</div>