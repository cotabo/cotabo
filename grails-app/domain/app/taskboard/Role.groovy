package app.taskboard

class Role {

	String authority

	static mapping = {
		table 'TB_ROLE'
		cache true
	}

	static constraints = {
		authority blank: false, unique: true
	}
	
	@Override
	public String toString() {
		return authority
	}
}
