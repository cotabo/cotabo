package org.cotabo

/**
 * Marker interface - classes who implements this need to make sure
 * that an action with the name that getRerenderAction returns - exists on their corresponding controller
 * 
 * @author Robert Krombholz
 *
 */
interface Rerenderable {
	public String getRerenderAction();
}
