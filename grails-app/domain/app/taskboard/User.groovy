package app.taskboard

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
	
	List adminBoards
	List userBoards
	
	static hasMany = [adminBoards: Board, userBoard: Board]
	static belongsTo = Board
	
		

	static constraints = {
		firstname blank: false
		lastname blank: false
		email blank: false, email: true
		username blank: false, unique: true
		password blank: false, minSize: 5				
	}

	static mapping = {
		table 'TB_USER'		
		password column: '`password`'
	}

	Set<Role> getAuthorities() {
		UserRole.findAllByUser(this).collect { it.role } as Set
	}
	
	@Override
	public String toString() {
		return username
	}
}
