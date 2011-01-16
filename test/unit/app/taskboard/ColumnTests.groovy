package app.taskboard

import grails.test.*

class ColumnTests extends GrailsUnitTestCase {
    protected void setUp() {
        super.setUp()
		mockDomain(Column)
		mockDomain(Board)
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

    void testColumnCreation() {
		//Preparation
		def user = User.findByUsername('testuser')
		assertNotNull user
		def myboard = new Board(name:'myboard')
			.addToUsers(user)
			
		def column = new Column(name:'mycolumn', board:myboard)
		
		assertTrue column.validate()				
		assertNotNull column.save()	
    }
	
	void testFailNameNull() {
		//Preparation
		def user = User.findByUsername('testuser')
		assertNotNull user
		def myboard = new Board(name:'myboard')
			.addToUsers(user)
			
		def column = new Column(board:myboard)
		assertFalse column.validate()
		assertEquals 1, column.errors.allErrors.size()
		assertNull column.save()
	}
	
	void testFailNameTooLong() {
		//Preparation
		def user = User.findByUsername('testuser')
		assertNotNull user
		def myboard = new Board(name:'myboard')
			.addToUsers(user)
			
		def column = new Column(name:'123456789012345678901234567',board:myboard)
		assertFalse column.validate()
		assertEquals 1, column.errors.allErrors.size()
		assertNull column.save()
	}
	
	void testFailTooHighLimit() {
		//Preparation
		def user = User.findByUsername('testuser')
		assertNotNull user
		def myboard = new Board(name:'myboard')
			.addToUsers(user)
			
		def column = new Column(name:'mycolumn', limit:76, board:myboard)
		assertFalse column.validate()
		assertEquals 1, column.errors.allErrors.size()
		assertNull column.save()
	}
	
	void testFailTooLongDescription() {
		//Preparation
		def user = User.findByUsername('testuser')
		assertNotNull user
		def myboard = new Board(name:'myboard')
			.addToUsers(user)
			
		def desc = '''
			This text is longer than 255 characters.
			This text is longer than 255 characters.
			This text is longer than 255 characters.
			This text is longer than 255 characters.
			This text is longer than 255 characters.
			This text is longer than 255 characters.
			This text is longer than 255 characters.
		'''	
		def column = new Column(name:'mycolumn', description: desc, board:myboard)
		
		assertFalse column.validate()
		assertEquals 1, column.errors.allErrors.size()
		assertNull column.save()
	}
	
	void testFailOnBoardNull() {
		def column = new Column(name:'mycolumn')
		
		assertFalse column.validate()
		assertEquals 1, column.errors.allErrors.size()
		assertNull column.save()
	}
	

}
