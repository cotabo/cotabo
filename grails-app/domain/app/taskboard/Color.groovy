package app.taskboard

/**
* Represents the Color of a Task object
*
* @author Robert Krombholz
*
*/
class Color {
	
	String colorCode

    static constraints = {
		colorCode nullable:false, minSize:7, maxSize:7, unique:true, validator: { val, obj ->
			val.startsWith('#')
		}	
    }
	
	@Override
	public String toString() {
		return colorCode	
	}
}
