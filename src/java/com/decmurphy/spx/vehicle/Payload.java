package com.decmurphy.spx.vehicle;

public abstract class Payload
{
	protected double mMass;

	public Payload()
	{
	}

	public Payload(Payload p)
	{
		this.mMass = p.mMass;
	}

	public void setMass(double mass)
	{
		mMass = mass;
	}

	public double getMass()
	{
		return mMass;
	}
}
