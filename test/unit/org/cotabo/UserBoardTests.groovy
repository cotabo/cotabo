package org.cotabo

import grails.test.*

class UserBoardTests extends GrailsUnitTestCase {
    protected void setUp() {
        super.setUp()
		mockDomain(User,[
			new User(
				username: 'testuser',
				password: 'testpassword',
				firstname: 'firstname',
				lastname: 'lastname',
				email: 'e@mail.com'
			)]
		)				
		mockDomain(UserBoard)		
		mockDomain(Column, [new Column(name:'todo')])
		mockDomain(Board, [
			new Board(
				name: 'testBoard',				
				columns: [Column.findByName('todo')]		
			)
		])		
		
		//Need to mock static executeUpdate as it is not mocked by mockDomain
		//See executed HQL in UserBoard.removeAll to understand this
		UserBoard.metaClass.static.executeUpdate = {String hql, Map props ->
			def userBoards
			if(props.user) {
				userBoards = UserBoard.findAllByUser(props.user)				
			}
			else if(props.board) {
				userBoards = UserBoard.findAllByBoard(props.board)
			}
			if(props.role) {
				userBoards = userBoards.findAll{it.role == props.role}
			}
			userBoards.each {it.delete()}
		}
		
		//Somehow the findAllByBoardAndUser doesn't work. mockDomain somehow sucks.
		UserBoard.metaClass.static.findAllByBoardAndUser = { Board board, User user ->
			UserBoard.list().findAll { it.user == user && it.board == board }
        }
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testCreateRelationshipAsAdmin() {		
		def user = User.findByUsername('testuser')
		def board = Board.findByName('testBoard')
		//Create the relationship
		UserBoard.create(user, board, RoleEnum.ADMIN)	
		
		def persistedUserBoard = UserBoard.findByRole(RoleEnum.ADMIN)
		assertNotNull persistedUserBoard 
		assertEquals user.username, persistedUserBoard.user.username
		assertEquals board.name, persistedUserBoard.board.name
		assertEquals RoleEnum.ADMIN, persistedUserBoard.role
    }
	
	void testRemove() {
		def user = User.findByUsername('testuser')
		def board = Board.findByName('testBoard')
		//Create the relationship
		UserBoard.create(user, board, RoleEnum.ADMIN, true)	
				
		assertEquals 1, UserBoard.list().size()		
		UserBoard.remove(user, board, RoleEnum.ADMIN)
		assertEquals 0, UserBoard.list().size()		
	}
	
	void testRemoveAllGivenUser() {
		new User(
			username: 'testuser2',
			password: 'testpassword',
			firstname: 'firstname',
			lastname: 'lastname',
			email: 'e@mail.com'
		).save()
		new Board(
			name: 'testBoard2',
			columns: [Column.findByName('todo')]
		).save()
		def user = User.findByUsername('testuser')
		def user2 = User.findByUsername('testuser2')
		def board = Board.findByName('testBoard')
		def board2 = Board.findByName('testBoard2')
		
		//Creating 2 relationships for this user
		UserBoard.create user, board, RoleEnum.ADMIN		
		UserBoard.create user, board2, RoleEnum.ADMIN
		
		assertEquals 2, UserBoard.list().size()
		UserBoard.removeAll(user)
		assertEquals 0, UserBoard.list().size()
		
		//Now using one as ADMIN and one as USER
		UserBoard.create user, board, RoleEnum.ADMIN
		UserBoard.create user, board2, RoleEnum.USER
		
		assertEquals 2, UserBoard.list().size()
		UserBoard.removeAll(user, RoleEnum.ADMIN)
		assertEquals 1, UserBoard.list().size()			
	}
	
	void testRemoveAllGivenBoard() {
		new User(
			username: 'testuser2',
			password: 'testpassword',
			firstname: 'firstname',
			lastname: 'lastname',
			email: 'e@mail.com'
		).save()
		new Board(
			name: 'testBoard2',
			columns: [Column.findByName('todo')]
		).save()
		def user = User.findByUsername('testuser')
		def user2 = User.findByUsername('testuser2')
		def board = Board.findByName('testBoard')
		def board2 = Board.findByName('testBoard2')
		
		//Creating 2 relationships for this Board
		UserBoard.create user, board, RoleEnum.ADMIN
		UserBoard.create user2, board, RoleEnum.ADMIN
		
		assertEquals 2, UserBoard.list().size()
		UserBoard.removeAll(board)
		assertEquals 0, UserBoard.list().size()
		
		//Now using twi different boards
		UserBoard.create user, board, RoleEnum.ADMIN
		UserBoard.create user, board2, RoleEnum.ADMIN
		
		assertEquals 2, UserBoard.list().size()
		UserBoard.removeAll(board, RoleEnum.ADMIN)
		assertEquals 1, UserBoard.list().size()
		//Only the assoc for board2 should be left
		assertEquals board2, UserBoard.list()[0].board
	}
}
