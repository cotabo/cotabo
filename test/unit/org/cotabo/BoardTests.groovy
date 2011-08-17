package org.cotabo

import grails.test.*

class BoardTests extends TaskBoardUnitTest {
    protected void setUp() {
		super.setUp()     
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testNewBoard() {
		//Preparation - get the user
		def user = User.findByUsername('testuser')
		assertNotNull user
		//Preparation - Column definition without save
		def column = Column.findByName('todo')							
		
		def board = new Board(name:'MyNewBoard')			
			.addToColumns(column)							
		
		def result = board.validate()		
		assertTrue result
		
		assertNotNull board.save()
		def returnedBoard = Board.findByName('MyNewBoard') 
		assertNotNull returnedBoard
		assertNotNull returnedBoard.columns		
		assertEquals 'todo', returnedBoard.columns.first().name							
    }
	
	void testFailWihoutColumns() {
		//Preparation - get the user
		def user = User.findByUsername('testuser')							
		assertNotNull user
							
		def board = new Board(name:'MyNewBoard')			
			
		assertFalse board.validate()		
		assertEquals 1, board.errors.allErrors.size()		
		assertNull board.save()
	}
	
	
	void testFailWihoutName() {
		//Preparation - get the user
		def user = User.findByUsername('testuser')		
		assertNotNull user
		//Preparation - Column definition without save
		def column = Column.findByName('todo')
							
		def board = new Board()			
			.addToColumns(column)						
			
		assertFalse board.validate()
		assertEquals 1, board.errors.allErrors.size()		
		assertNull board.save()
	}
	
	void testFailTooLongDescription() {		
		//Preparation - get the user
		def user = User.findByUsername('testuser')						
		assertNotNull user
		//Preparation - Column definition without save
		def column = Column.findByName('todo')	
					
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
		
		def board = new Board(name:'MyNewBoard', description: description)			
			.addToColumns(column)						
			
		assertFalse board.validate()
		assertEquals 1, board.errors.allErrors.size()
		assertNull board.save()
	}

	void testFailTooMuchColumns() {
		//Preparation - get the user
		def user = User.findByUsername('testuser')						
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
		
		def board = new Board(name:'MyNewBoard')								
			
		columnList.each {
			board.addToColumns(it)
		}	
								
		assertFalse board.validate()
		assertEquals 1, board.errors.allErrors.size()
		assertNull board.save()
	}	
}
