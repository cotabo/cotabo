package app.taskboard

import grails.test.*

class BoardTests extends GrailsUnitTestCase {
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
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testNewBoard() {
		//Preparation - get the user
		def user = User.findByUsername('testuser')
		assertNotNull user
		//Preparation - Column definition without save
		def column = new Column(name:'mycolumn')
		def color = Color.findByColorCode('#FFFFFF')
		def priority = Priority.findByName('Critical')	
		assertNotNull color
		assertNotNull priority	
		
		def board = new Board(name:'myboard', defaultColor: color, defaultPriority: priority)
			.addToUsers(user)
			.addToColumns(column)
			.addToAdmins(user)
			.addToColors(color)
			.addToPriorities(priority)
		
			
		assertTrue board.validate()
		assertNotNull board.save()
		def returnedBoard = Board.findByName('myboard') 
		assertNotNull returnedBoard
		assertNotNull returnedBoard.columns
		assertNotNull returnedBoard.users
		assertEquals 'mycolumn', returnedBoard.columns.first().name
		assertEquals 'testuser', returnedBoard.users.first().username						
    }
	
	void testFailWihoutColumns() {
		//Preparation - get the user
		def user = User.findByUsername('testuser')
		def color = Color.findByColorCode('#FFFFFF')
		def priority = Priority.findByName('Critical')
		assertNotNull color
		assertNotNull priority
		assertNotNull user
							
		def board = new Board(name:'myboard', defaultColor: color, defaultPriority: priority)
			.addToUsers(user)
			.addToAdmins(user)
			.addToColors(color)
			.addToPriorities(priority)
			
		assertFalse board.validate()
		assertEquals 1, board.errors.allErrors.size()
		assertNull board.save()
	}
	
	void testFailWithoutUsers() {		
		//Preparation - get the user
		def user = User.findByUsername('testuser')
		def color = Color.findByColorCode('#FFFFFF')
		def priority = Priority.findByName('Critical')
		assertNotNull color
		assertNotNull priority
		assertNotNull user
		//Preparation - Column definition without save
		def column = new Column(name:'mycolumn')
							
		def board = new Board(name:'myboard', defaultColor: color, defaultPriority: priority)
			.addToColumns(column)
			.addToAdmins(user)
			.addToColors(color)
			.addToPriorities(priority)
			
		assertFalse board.validate()
		assertEquals 1, board.errors.allErrors.size()
		assertNull board.save()
	}
	
	void testFailWihoutName() {
		//Preparation - get the user
		def user = User.findByUsername('testuser')
		def color = Color.findByColorCode('#FFFFFF')
		def priority = Priority.findByName('Critical')
		assertNotNull color
		assertNotNull priority
		assertNotNull user
		//Preparation - Column definition without save
		def column = new Column(name:'mycolumn')
							
		def board = new Board(defaultColor: color, defaultPriority: priority)
			.addToUsers(user)
			.addToColumns(column)
			.addToAdmins(user)
			.addToColors(color)
			.addToPriorities(priority)
			
		assertFalse board.validate()
		assertEquals 1, board.errors.allErrors.size()
		assertNull board.save()
	}
	
	void testFailTooLongDescription() {		
		//Preparation - get the user
		def user = User.findByUsername('testuser')
		def color = Color.findByColorCode('#FFFFFF')
		def priority = Priority.findByName('Critical')
		assertNotNull color
		assertNotNull priority
		assertNotNull user
		//Preparation - Column definition without save
		def column = new Column(name:'mycolumn')
					
		def description = '''
			This test is longer than 255 Characters.
			This test is longer than 255 Characters.
			This test is longer than 255 Characters.
			This test is longer than 255 Characters.
			This test is longer than 255 Characters.
			This test is longer than 255 Characters.
			This test is longer than 255 Characters.
			This test is longer than 255 Characters.
			This test is longer than 255 Characters.
			This test is longer than 255 Characters.
			This test is longer than 255 Characters.
			This test is longer than 255 Characters.
		'''
		
		def board = new Board(name:'myboard', description: description, defaultColor: color, defaultPriority: priority)
			.addToUsers(user)
			.addToColumns(column)
			.addToAdmins(user)
			.addToColors(color)
			.addToPriorities(priority)
			
		assertFalse board.validate()
		assertEquals 1, board.errors.allErrors.size()
		assertNull board.save()
	}

	void testFailTooMuchColumns() {
		//Preparation - get the user
		def user = User.findByUsername('testuser')
		def color = Color.findByColorCode('#FFFFFF')
		def priority = Priority.findByName('Critical')
		assertNotNull color
		assertNotNull priority
		assertNotNull user
		
		def columnList = [
			new Column(name:'mycolumn1'),
			new Column(name:'mycolumn2'),
			new Column(name:'mycolumn3'),
			new Column(name:'mycolumn4'),
			new Column(name:'mycolumn5'),
			new Column(name:'mycolumn6'),
			new Column(name:'mycolumn7'),
			new Column(name:'mycolumn8'),
			new Column(name:'mycolumn9'),
			new Column(name:'mycolumn10'),
			new Column(name:'mycolumn11'),
		]
		
		def board = new Board(name:'myboard', defaultColor: color, defaultPriority: priority)
			.addToUsers(user)		
			.addToAdmins(user)
			.addToColors(color)
			.addToPriorities(priority)
			
		columnList.each {
			board.addToColumns(it)
		}	
								
		assertFalse board.validate()
		assertEquals 1, board.errors.allErrors.size()
		assertNull board.save()
	}
	
	void testFailWithoutAdmin() {
		//Preparation - get the user
		def user = User.findByUsername('testuser')
		def color = Color.findByColorCode('#FFFFFF')
		def priority = Priority.findByName('Critical')
		assertNotNull color
		assertNotNull priority
		assertNotNull user
		def column = new Column(name:'mycolumn')
							
		def board = new Board(name:'myboard', defaultColor: color, defaultPriority: priority)
			.addToUsers(user)
			.addToColumns(column)
			.addToColors(color)
			.addToPriorities(priority)
			
		assertFalse board.validate()
		assertEquals 1, board.errors.allErrors.size()
		assertNull board.save()
	}
	
	void testFailTooMuchAdmins() {
		//Preparation - get the user
		def user = User.findByUsername('testuser')
		def color = Color.findByColorCode('#FFFFFF')
		def priority = Priority.findByName('Critical')
		assertNotNull color
		assertNotNull priority
		assertNotNull user
		def column = new Column(name:'mycolumn')
		
		//Calling addToAdmins 6 times. Limit is 5.
		def board = new Board(name:'myboard', defaultColor: color, defaultPriority: priority)
			.addToUsers(user)
			.addToColumns(column)
			.addToColors(color)
			.addToPriorities(priority)
			.addToAdmins(user)
			.addToAdmins(user)
			.addToAdmins(user)
			.addToAdmins(user)
			.addToAdmins(user)
			.addToAdmins(user)
		assertFalse board.validate()
		assertEquals 1, board.errors.allErrors.size()
		assertNull board.save()
		
	}
	
	void testFailWithoutDefaultValues() {
		//Preparation - get the user
		def user = User.findByUsername('testuser')
		def color = Color.findByColorCode('#FFFFFF')
		def priority = Priority.findByName('Critical')
		assertNotNull color
		assertNotNull priority
		assertNotNull user
		def column = new Column(name:'mycolumn')
		
		
		def board = new Board(name:'myboard')
			.addToUsers(user)
			.addToColumns(column)
			.addToAdmins(user)
			.addToColors(color)
			.addToPriorities(priority)

		assertFalse board.validate()
		board.errors.each { println it }
		assertEquals 2, board.errors.allErrors.size()
		assertNull board.save()
	}
	
	void testFailWithoutColorsAndPriorities() {
		//Preparation - get the user
		def user = User.findByUsername('testuser')
		def color = Color.findByColorCode('#FFFFFF')
		def priority = Priority.findByName('Critical')
		assertNotNull color
		assertNotNull priority
		assertNotNull user
		def column = new Column(name:'mycolumn')
		
		def board = new Board(name:'myboard', defaultColor: color, defaultPriority: priority)
			.addToUsers(user)
			.addToColumns(column)
			.addToAdmins(user)

		assertFalse board.validate()
		board.errors.each { println it }
		assertEquals 2, board.errors.allErrors.size()
		assertNull board.save()
	}
}
