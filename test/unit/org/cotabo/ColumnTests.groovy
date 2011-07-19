package org.cotabo

import grails.test.*

class ColumnTests extends TaskBoardUnitTest {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testColumnCreation() {
		def myboard = Board.findByName('myboard')
			
		def column = new Column(name:'mycolumn', board:myboard)
		
		assertTrue column.validate()				
		assertNotNull column.save()	
    }
	
	void testFailNameNull() {
		def myboard = Board.findByName('myboard')
			
		def column = new Column(board:myboard)
		assertFalse column.validate()
		assertEquals 1, column.errors.allErrors.size()
		assertNull column.save()
	}
	
	void testFailNameTooLong() {
		def myboard = Board.findByName('myboard')
			
		def column = new Column(name:'123456789012345678901234567',board:myboard)
		assertFalse column.validate()
		assertEquals 1, column.errors.allErrors.size()
		assertNull column.save()
	}
	
	void testFailTooHighLimit() {
		def myboard = Board.findByName('myboard')
		
		def column = new Column(name:'mycolumn', limit:76, board:myboard)
		assertFalse column.validate()
		assertEquals 1, column.errors.allErrors.size()
		assertNull column.save()
	}
	
	void testFailTooLongDescription() {
		def myboard = Board.findByName('myboard')
			
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
