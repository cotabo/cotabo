package org.cotabo

class TaskColor {
	
	static belongsTo = [Task, Board]
	static hasMany = [tasks : Task]
	
    static constraints = {
		name nullable:false, unique:true, blank:false
		color nullable:false, validator: {val, obj -> try {java.awt.Color.decode(val)} catch (Exception e){return false}; return true}
    }
	
	static exportables = ['name', 'color']
	
	String name
	String color
	
	@Override
	public String toString(){
		return color;
	}
}
