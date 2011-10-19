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
			<p class="description">${attrs.board?.description?.encodeAsHTML() ?: ''}</p>
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
		def width = (100 / attrs.column.board.columns.size()) -0.2 ;
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
		if (!attrs.task || attrs.task.archived) {
			out << ''			
		}
		else {
			def lastColumn = attrs.task.column.board.columns.last()
			out << """
			<li class="ui-widget ui-corner-all" id="task_${attrs.task.id}">
				<div class="task-header ui-state-default">"""
           if(attrs.task?.assignee?.avatar){
               out << """
					<img class="ui-icon ui-icon-person avatar" src="${createLink(controller:'user', action:'avatar', id:attrs.task?.assignee?.username)}" title="${attrs.task?.assignee}"/>"""
           }
           else{
               out << """
					<span class="ui-icon ui-icon-person avatar" title="${attrs.task?.assignee}"></span>"""
           }
           
		   attrs.task?.colors?.each{color ->
			   out << """
					<div class="head_color" style="background-color:${color?.color?.encodeAsHTML()};"></div>"""
		   }
		   
           out << """
					<div class="head_name">#${attrs.task?.id ?: ''} - ${attrs.task?.name?.encodeAsHTML()}</div>"""
				if(attrs.task.column.id == lastColumn.id) {
					out << """
					<a href="${createLink(controller:'task', action:'archive', id:attrs.task?.id)}" class="ui-icon ui-icon-disk archive" title="archive"></a>"""
				}
				out << """
					<span class="block-box ui-icon ${attrs.task?.blocked ? 'ui-icon-locked blocked' : 'ui-icon-unlocked not-blocked'}" title="${attrs.task?.blocked ? 'blocked' : 'unblocked'}"></span>
					<span class="expander ui-icon ui-icon-carat-1-${attrs.hide ? 's' : 'n'}" ></span>
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
								<td><b>Details:</b></td>
								<td id="task_${attrs.task.id}_details">${attrs.task?.details?.encodeAsHTML()?.replaceAll("\n", "<br/>") ?: ''}</td>
							</tr>
							<tr>
								<td><b>Priority:</b></td>
								<td id="task_${attrs.task.id}_priority">${attrs.task?.priority?.encodeAsHTML()}</td>
							</tr>
							<tr>
								<td><b>Assignee:</b></td>
								<td id="task_${attrs.task.id}_assignee">${attrs.task?.assignee?.username?.encodeAsHTML() ?: ''}</td>
							</tr>
						</tbody>
					</table>
				</div>
			</li>
			"""	
		}				
	}
	
	/**
	 * Print a board summary for listing
	 * attrs:
	 * 		board: board Object (required)
	 * 		admin: (boolean) whether this summary should contain admin options or not
	 */
	def boardSummary = { attrs, body ->
		if(!attrs.board) {
			out << ''
			return
		}
		out << """
		<li class="board_summary ui-widget ui-corner-all">
			<div class="summary_header ui-state-default">
				
				<a href='${createLink(conroller:"board", action:"show", id:"${attrs.board.id}")}' class="highlighted_link ">
					<span class="ui-icon ui-state-default ui-icon-folder-open"></span>
					${attrs.board.name.encodeAsHTML()}
				</a>
				<ul>
					<li title="number of users">
						<span class="ui-icon  ui-icon-person"></span>
						${attrs.board.users.size()}						
					</li>
					<li title="reporting">
						<a href="${createLink(conroller:"board", action:"comulativeflowchart", id:"${attrs.board.id}")}">
							<span class="ui-icon ui-state-default ui-icon-image"></span>						
						</a>
					</li>
		"""
		if(attrs.admin) {
			out << """
					<li title="edit this board">
						<a href="${createLink(conroller:"board", action:"edit", id:"${attrs.board.id}")}">
							<span class="ui-icon ui-state-default ui-icon-wrench"></span>
						</a>
					</li>
					<li title="delete this board">
						<a class="delete" href="${createLink(conroller:"board", action:"delete", id:"${attrs.board.id}")}">
							<span class="ui-icon ui-state-default ui-icon-trash"></span>
						</a>
					</li>
			"""
		}
		out << """
				</ul>
			</div>
			
			<div class="summary_content ui-widget-content">
				${attrs.board.description ? attrs.board.description?.encodeAsHTML(): '&nbsp;'}
				<table>
					<tbody>
						<tr>
							<td><span class="ui-icon ui-icon-gear"></span>open tasks:</td>								
							<td class="highlighted_link">
								${attrs.board.columns.collect{it == attrs.board.columns.last()? 0 : it.tasks.size()}.sum()}
							</td>
						</tr>
						<tr>
							<td><span class="ui-icon ui-icon-gear"></span>tasks done:</td>								
							<td class="highlighted_link">${attrs.board.columns.last().tasks.size()}</td>
						</tr>
						<tr>
							<td><span class="ui-icon ui-icon-gear"></span>overall tasks:</td>
							<td class="highlighted_link">${attrs.board.columns.collect{it.tasks.size()}.sum()}</td>
						</tr>
					</tbody>
				</table>
			</div>
		</li>
		"""
		
	}
	/**
	 * Prints a role of org.cotabo.RoleEnum in a user-friendly format
	 * 	attrs:
	 * 	  - role: the RoleEnum value to print
	 */
	def printRole = { attrs ->
		if (!attrs.role)
			return		
		def friendly = attrs.role.toString().toLowerCase()
		friendly = friendly.replaceAll('_', ' ')
		out << friendly
	}
}
