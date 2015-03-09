package com.decmurphy.spx.payload;

import com.decmurphy.spx.util.Payload;

public abstract class RawPayload
{
	protected double mMass;
	private Payload payload;

	public RawPayload() {
	}

	public RawPayload(RawPayload p) {
		this.mMass = p.mMass;
	}
	
	public void setPayloadType(Payload p) {
		this.payload = p;
	}
	public Payload getPayloadType() {
		return payload;
	}

	public void setMass(double mass) {
		mMass = mass;
	}
	public double getMass() {
		return mMass;
	}
}
