package org.cotabo

import java.io.Serializable
import org.apache.commons.lang.builder.HashCodeBuilder

/**
 * Resolves the n-m relation between Board & User + adding a Role property
 * Most of this is borrowed from spring security core 
 * (https://github.com/grails-plugins/grails-spring-security-core/blob/master/src/templates/PersonAuthority.groovy.template) 
 * but modified to hold an additional enum.
 * 
 * @author Robert Krombholz 
 */
class UserBoard implements Serializable{

	User user
	Board board
	RoleEnum role 
	
	boolean equals(other) {
		if (!(other instanceof UserBoard)) {
			return false
		}

		other.user?.id == user?.id &&
			other.board?.id == board?.id
	}

	int hashCode() {
		def builder = new HashCodeBuilder()
		if (user) builder.append(user.id)
		if (board) builder.append(board.id)
		builder.toHashCode()
	}

	static UserBoard get(long userId, long boardId, RoleEnum role = null) {
		if (role) {
			find 'from UserBoard where user.id=:userId and board.id=:boardId and role=:role',
				[userId: userId, boardId: boardId, role:role]
		}
		else {
			find 'from UserBoard where user.id=:userId and board.id=:boardId',
				[userId: userId, boardId: boardId]
		}
		
	}

	static UserBoard create(User user, Board board, RoleEnum role, boolean flush = false) {
		new UserBoard(user: user, board: board, role:role).save(flush: flush, insert: true)
	}

	static boolean remove(User user, Board board, RoleEnum role, boolean flush = false) {
		List userBoardList = UserBoard.findAllByBoardAndUser(board, user)		
		UserBoard instance = userBoardList.find { it.role == role }			
		instance ? instance.delete(flush: flush) : false
	}

	static void removeAll(User user, RoleEnum role = null) {
		if(role) {
			executeUpdate 'DELETE FROM UserBoard WHERE user=:user AND role=:role', [user:user, role:role]
		}
		else {
			executeUpdate 'DELETE FROM UserBoard WHERE user=:user', [user: user]
		}
		
	}

	static void removeAll(Board board, RoleEnum role = null) {
		if(role) {
			executeUpdate 'DELETE FROM UserBoard WHERE board=:board AND role=:role', [board:board, role:role]
		}
		else {
			executeUpdate 'DELETE FROM UserBoard WHERE board=:board', [board: board]
		}
		
	}

	static mapping = {
		//Not including the role into the primaryKey as this enables uniqueness of UserBoards
		id composite: ['board', 'user']		
		version false
	}
	
	static constrains = {
		user nullable:false
		board nullable:false
		role nullable:false
	}
}
