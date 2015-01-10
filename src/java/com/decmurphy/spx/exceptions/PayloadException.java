package com.decmurphy.spx.exceptions;

/**
 *
 * @author declan
 */
public class PayloadException extends Exception {

	public PayloadException(String reason) {
		System.out.println("ERROR: " + reason);
	}
}
