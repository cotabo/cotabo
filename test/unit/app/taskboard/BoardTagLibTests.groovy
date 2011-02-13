package app.taskboard


import grails.test.*

class BoardTagLibTests extends TagLibUnitTestCase {
    protected void setUp() {
        super.setUp()		
		mockDomain(Board)
		mockDomain(Column)
		mockDomain(Color, [new Color(colorCode: '#FFFFFF')])
		mockDomain(Priority, [new Priority(name: 'Critical')])
		mockDomain(User, [
			new User(
				username: 'testuser',
				password: 'testpassword',
				firstname: 'firstname',
				lastname: 'lastname',
				email: 'e@mail.com'
			)]
		)
		//Preparation - get the user
		def user = User.findByUsername('testuser')
		assertNotNull user
		//Preparation - Column definition without save
		def column = new Column(name:'koffer', limit: 5)
		def color = Color.findByColorCode('#FFFFFF')
		def priority = Priority.findByName('Critical')	
		
		def board = new Board(name:'MyBoard', defaultColor: color, defaultPriority: priority)
			.addToUsers(user)
			.addToColumns(column)			
			.addToAdmins(user)
			.addToColors(color)
			.addToPriorities(priority)
			
		board.save()
		mockTagLib(BoardTagLib)
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testBoard() {
		def expected = """
		<div id="board">
			<h2>MyBoard</h2>
		
		</div>
		""" 	
			
		def tl = new BoardTagLib()
		tl.board([board:Board.findByName('MyBoard')], {})
		assertEquals  expected,  tl.out.toString()
    }
	
	void testColumn() {
		def expected = """
		<div class="column" style="width:33%;">
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
		<li class="ui-widget" id="task_1">
			<div class="task-header ui-state-default">
				<div>mytask</div>
				<span class="ui-icon ui-icon ui-icon-arrowthickstop-1-n"/>
			</div>
			<div class="task-content ui-widget-content">
				<table>
					<tbody>
						<tr>
							<td><b>Description:</b></td>
							<td>test description</td>
						</tr>
						<tr>
							<td><b>Priority:</b></td>
							<td>critical</td>
						</tr>
						<tr>
							<td><b>Assignee:</b></td>
							<td>firstname lastname</td>
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
			color: new Color(colorCode:'#FFFFFF'), 
			priority: new Priority(name:'critical'), 
			column: new Column(id: 1, name:'koffer', limit: 5), 
			creator: user,
			assignee: user
		)
		
		def tl = new BoardTagLib()
		tl.task([task:theTask], {''})
		assertEquals expected, tl.out.toString()
		
	}
}
