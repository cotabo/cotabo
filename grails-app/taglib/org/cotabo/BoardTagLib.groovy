package org.cotabo

class BoardTagLib {
	static namespace = "tb"
	
	
	/**
	 * Prints a role of org.cotabo.RoleEnum in a user-friendly format
	 * 	attrs:
	 * 	  - role: the RoleEnum value to print
	 */
	def printRole = { attrs ->
		if (!attrs.role)
			return		
		def friendly = attrs.role.toString().toLowerCase()
		friendly = friendly.replaceAll('_', ' ')
		out << friendly
	}
}
