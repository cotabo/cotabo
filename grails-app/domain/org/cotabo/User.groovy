package org.cotabo

class User {

	String username
	String password	
	//Stuff neded by Spring Security plugin - won't use them.
	boolean enabled = true
	boolean accountExpired = false
	boolean accountLocked = false
	boolean passwordExpired = false
	
	String firstname
	String lastname
	String email	

	byte[] avatar
	String avatarType
			

	static constraints = {
		firstname blank: false
		lastname blank: false
		email blank: false, email: true
		username blank: false, unique: true
		password blank: false, minSize: 5				
		avatar nullable: true, maxSize: 102400
		avatarType nullable: true
	}

	static mapping = {
		table 'TB_USER'		
		password column: '`password`'
	}

	Set<Role> getAuthorities() {
		UserRole.findAllByUser(this).collect { it.role } as Set
	}
	
	/**
	 * Returns all related boards of this user.
	 * See role param below on returning behaviour
	 * @param role -<i>optional</i> RoleEnum value. If given returning only the related board 
	 * with this role. Otherwhise returning all related boards.
	 * 
	 * @return The related boards based on the given role (or all if no role given)
	 */
	Set<Board> getBoards(RoleEnum role = null) {
		if(role) {
			return UserBoard.findAllByUserAndRole(this,role).collect { it.board } as Set
		}
		else {
			return UserBoard.findAllByUser(this).collect { it.board } as Set
		}
	}
	
	@Override
	public String toString() {
		return "$username"
	}
}
