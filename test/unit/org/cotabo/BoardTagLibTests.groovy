package org.cotabo


import grails.test.*
import org.codehaus.groovy.grails.plugins.codecs.HTMLCodec

class BoardTagLibTests extends TagLibUnitTestCase {
    protected void setUp() {
        super.setUp()	
		//Mocking encodeAsHtml()		
		String.metaClass.encodeAsHTML = {HTMLCodec.encode(delegate)}
		BoardTagLib.metaClass.createLink = {map ->
			return "/${map.controller}/${map.action}/${map.id}"
		}
		
		mockConfig '''
			taskboard.colors = ['#faf77a', '#fa7a88', '#bcbcf5', '#f9d7a9']
			taskboard.priorities = ['Critical', 'Major', 'Normal', 'Low']
		'''
		mockDomain(Board)
		mockDomain(Column)				
		mockDomain(User, [
			new User(
				username: 'testuser',
				password: 'testpassword',
				firstname: 'firstname',
				lastname: 'lastname',
				email: 'e@mail.com'
			)]
		)
		mockDomain(UserBoard)
		//Preparation - get the user
		def user = User.findByUsername('testuser')
		assertNotNull user
		//Preparation - Column definition without save
		def column = new Column(name:'koffer', limit: 5)				
		
		def board = new Board(name:'MyBoard')			
			.addToColumns(column)						
		board.save()
		UserBoard.create(user, board, RoleEnum.ADMIN)
		mockTagLib(BoardTagLib)
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testBoard() {
    	def expected = """
		<div id="board">
			<h2>MyBoard</h2>
			<p class="description"></p>
		
		</div>
		"""
			
		def tl = new BoardTagLib()
		tl.board([board:Board.findByName('MyBoard')], {})
		assertEquals  expected,  tl.out.toString()
    }
	
	void testColumn() {
		def expected = """
		<div class="column" style="width:33.2333333333%;">
			<span class="title">
				<p>koffer</p>
				<span>
					<p class="value">&nbsp;</p>
					<p>/</p>
					<p class="limit">5</p>
				</span>
			</span>
			<ul id="column_1">
		
			</ul>
		</div>
		"""		
		//Prepare a board with 3 columns
		def board = new Board(name:'KofferBoard')
		//Adding 3 columns because we expect a size of 33%
		def columns = [
			new Column(id: 1, name:'koffer', limit: 5, board:board),
			new Column(id: 2, name:'koffer2', limit: 5, board:board),
			new Column(id: 3, name:'koffer3', limit: 5, board:board)
		]
		board.columns = columns

					
		def tl = new BoardTagLib()
		tl.column([column:columns[0]], {''})
		assertEquals expected, tl.out.toString()			
	}
	
	void testTask() {
		def expected= """
			<li class="ui-widget ui-corner-all" id="task_1">
				<div class="task-header ui-state-default">
					<img class="avatar" src="/user/avatar/testuser" />
					<div class="head_color" style="background-color:#faf77a;"></div>
					<div id="color_helper" style="display:none;">#faf77a</div>
					<div class="head_name">#1 - mytask</div>
					<div class="block-box not-blocked"></div>
					<span class="ui-icon ui-icon-carat-1-n"/>
				</div>
				<div class="task-content ui-widget-content" style="display:block">
					<table>
						<colgroup>
							<col width="25%"/>
							<col width="75%"/>
						</colgroup>
						<tbody>
							<tr>
								<td><b>Description:</b></td>
								<td id="task_1_description">test description</td>
							</tr>
							<tr>
								<td><b>Priority:</b></td>
								<td id="task_1_priority">Critical</td>
							</tr>
							<tr>
								<td><b>Assignee:</b></td>
								<td id="task_1_assignee">testuser</td>
							</tr>
						</tbody>
					</table>
				</div>
			</li>
			"""
		
		def user = User.findByUsername('testuser')
		def theTask = new Task(
			id:1,
			name: 'mytask', 
			description: 'test description',
			durationHours: 0.5, 						
			column: new Column(id: 1, name:'koffer', limit: 5), 
			creator: user,
			assignee: user,
			color: '#faf77a',
			priority: 'Critical'
		)
		
		def tl = new BoardTagLib()
		tl.task([task:theTask], {''})
		assertEquals expected, tl.out.toString()
		
	}
}
