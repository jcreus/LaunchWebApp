package com.decmurphy.spx.exceptions;

/**
 *
 * @author declan
 */
public class ProfileException extends Exception {

	public ProfileException(String reason) {
		System.out.println("ERROR: " + reason);
	}
}
