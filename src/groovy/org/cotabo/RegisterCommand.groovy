package org.cotabo

/**
 * RegisterCommand for ease of input validation of the registration process.
 * Has got the same validation rules as the User domain class but with this we can leave out the
 * parameter <> User object mapping code. 
 * Onle the constraint for the roles list is added here
 * 
 * @author Robert Krombholz
 */
class RegisterCommand {
	String username
	String password
	String firstname
	String lastname
	String email
	List<String> roles = []
	
	static constraints = {
		firstname blank: false
		lastname blank: false
		email blank: false, email: true
		username blank: false
		password blank: false, minSize: 5
	}
}
