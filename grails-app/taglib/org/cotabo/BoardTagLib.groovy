package org.cotabo

class BoardTagLib {
	static namespace = "tb"
	
	/**
	 * attrs:
	 * 	board	[required]
	 */
	def board = {attrs, body ->
		out << """
		<div id="board">
			<h2>${attrs.board?.name?.encodeAsHTML()}</h2>
		"""						
		body() ? out << body() :
		out << """
		</div>
		"""		
	}
	
	
	/**
	 * attrs: 
	 * 	column 	[required]
	 */
	def column = { attrs, body ->		
		//We're removing 0.1 % from each width to avoid a scrollbar when it reached 100%
		def width = (100 / attrs.column.board.columns.size()) -0.1 ;
		out << """
		<div class="column" style="width:${width}%;">
			<span class="title">
				<p>${attrs.column?.name?.encodeAsHTML()}</p>
				<span>
					<p class="value">&nbsp;</p>"""
		
		if (attrs.column?.limit > 0) {
			out << """
					<p>/</p>
					<p class="limit">${attrs.column?.limit?.toString().encodeAsHTML()}</p>"""
			
		}
		out << '''
				</span>
			</span>'''
		
		out << """
			<ul id="column_${attrs.column.id}">
		"""								
		out << body()		
		out << '''
			</ul>
		</div>
		'''					
	}
	
	
	/**
	 * attrs: 
	 * 	task 	[required]
	 *  hide    [true,false] > hides task content if setted to true
	 * 	
	 */
	def task = {attrs, body ->
		if (!attrs.task) {
			out << ''			
		}
		else {
			out << """
			<li class="ui-widget ui-corner-all" id="task_${attrs.task.id}">
				<div class="task-header ui-state-default">
					<img class="avatar" src="${createLink(controller:'user', action:'avatar', id:attrs.task?.assignee?.username)}" />
					<div class="head_color" style="background-color:${attrs.task?.color?.encodeAsHTML()};"></div>
					<div id="color_helper" style="display:none;">${attrs.task?.color?.encodeAsHTML()}</div>
					<div class="head_name">#${attrs.task?.id ?: ''} - ${attrs.task?.name?.encodeAsHTML()}</div>
					<div class="block-box ${attrs.task?.blocked ? 'blocked' : 'not-blocked'}"></div>
					<span class="ui-icon ui-icon-carat-1-${attrs.hide ? 's' : 'n'}"/>					
				</div>
				<div class="task-content ui-widget-content" style="display:${attrs.hide ? 'none' : 'block'}">
					<table>
						<colgroup>
							<col width="25%"/>
							<col width="75%"/>
						</colgroup>
						<tbody>
							<tr>
								<td><b>Description:</b></td>
								<td id="task_${attrs.task.id}_description">${attrs.task?.description?.encodeAsHTML() ?: ''}</td>
							</tr>
							<tr>
								<td><b>Priority:</b></td>
								<td id="task_${attrs.task.id}_priority">${attrs.task?.priority?.encodeAsHTML()}</td>
							</tr>
							<tr>
								<td><b>Assignee:</b></td>
								<td id="task_${attrs.task.id}_assignee">${attrs.task?.assignee?.toString()?.encodeAsHTML() ?: ''}</td>
							</tr>
						</tbody>
					</table>
				</div>
			</li>
			"""	
		}				
	}
}
