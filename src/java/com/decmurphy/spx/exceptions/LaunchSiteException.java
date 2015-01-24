package com.decmurphy.spx.exceptions;

/**
 *
 * @author declan
 */
public class LaunchSiteException extends Exception {
	
	public LaunchSiteException(String reason) {
		System.out.println("ERROR: " + reason);
	}
}
