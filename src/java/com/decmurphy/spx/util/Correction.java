package com.decmurphy.spx.util;

/**
 *
 * @author declan
 */
public enum Correction {

	PITCH,
	YAW,
	ROLL,
	THROTTLE;
	
	public static Correction findCorrectionType(String type) {
		try {
			return Correction.valueOf(type);
		} catch (Exception e) {
	  }
		return null;
	}
}
