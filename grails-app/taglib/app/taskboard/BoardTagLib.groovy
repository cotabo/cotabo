package app.taskboard

class BoardTagLib {
	static namespace = "tb"
	
	/**
	 * attrs:
	 * 	board	[required]
	 */
	def board = {attrs, body ->
		out << """
		<div id="board">
			<h1>${attrs.board?.name}</h1>
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
		def width = Math.round((100 / attrs.column.board.columns.size()) - 0.5d) ;		
		out << """
		<div class="column" style="width:${width}%;">
			<span class="title">
				<p>${attrs.column?.name}</p>
				<span>
					<p class="value">&nbsp;</p>"""
		
		if (attrs.column?.limit > 0) {
			out << """
					<p>/</p>
					<p class="limit">${attrs.column?.limit}</p>"""
			
		}
		out << '''
				</span>
			</span>'''
		
		out << """
			<ul id="${attrs.column?.id}">
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
	 * 	
	 */
	def task = {attrs, body ->
		out << """
		<li class="ui-widget">
			<div class="task-header ui-state-default">
				${attrs.task?.name}
				<span class="ui-icon ui-icon ui-icon-arrowthickstop-1-n"/>
			</div>
			<div class="task-content ui-widget-content">
				<table>
					<tbody>
						<tr>
							<td><b>Description:</b></td>
							<td>${attrs.task?.description}</td>
						</tr>
						<tr>
							<td><b>Priority:</b></td>
							<td>${attrs.task?.priority}</td>
						</tr>
						<tr>
							<td><b>Assignee:</b></td>
							<td>${attrs.task?.assignee?.firstname} ${attrs.task?.assignee?.lastname}</td>
						</tr>
					</tbody>
				</table>
			</div>
		</li>
		"""					
	}
}
