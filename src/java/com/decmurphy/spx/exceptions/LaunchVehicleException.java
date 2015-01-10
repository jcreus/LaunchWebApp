package com.decmurphy.spx.exceptions;

/**
 *
 * @author declan
 */
public class LaunchVehicleException extends Exception {
	
	public LaunchVehicleException(String reason) {
		System.out.println("ERROR: " + reason);
	}
}
