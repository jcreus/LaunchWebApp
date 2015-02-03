package com.decmurphy.spx.engine;

import static com.decmurphy.spx.Globals.g0;

public class Merlin1DVac extends Engine
{
	public Merlin1DVac()
	{
		setIsp(0, 345);
		setThrust(0, 801000);
		setMdot(getVacThrust()/(getVacIsp() * g0));
	}

	@Override
	public double getThrustAtAltitude(double altitude)
	{
		return this.getIspAtAltitude(altitude)*this.getMdot()*g0;
	}

	@Override
	public double getIspAtAltitude(double altitude)
	{
		return this.getVacIsp();
	}

}
