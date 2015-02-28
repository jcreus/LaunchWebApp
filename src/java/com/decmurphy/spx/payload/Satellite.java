package com.decmurphy.spx.payload;

/**
 *
 * @author dmurphy
 */
public class Satellite extends RawPayload {

	public Satellite() {
		setMass(500);
	}

	public Satellite(double Mass) {
		setMass(Mass);
	}
}
