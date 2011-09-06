package org.cotabo

class TaskColor {

    static constraints = {
		name nullable:false
		color nullable:false, validator: {val, obj -> try {java.awt.Color.decode(val)} catch (Exception e){return false}; return true}
    }
	
	String name
	String color
}
