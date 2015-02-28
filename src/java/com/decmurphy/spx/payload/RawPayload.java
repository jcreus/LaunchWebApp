package com.decmurphy.spx.payload;

public abstract class RawPayload
{
	protected double mMass;

	public RawPayload() {
	}

	public RawPayload(RawPayload p) {
		this.mMass = p.mMass;
	}

	public void setMass(double mass) {
		mMass = mass;
	}

	public double getMass() {
		return mMass;
	}
}
