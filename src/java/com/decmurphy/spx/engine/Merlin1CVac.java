package com.decmurphy.spx.engine;

import static com.decmurphy.utils.Globals.g0;

/**
 *
 * @author dmurphy
 */
public class Merlin1CVac extends Engine
{
	public Merlin1CVac()
	{
		setIsp(0, 342);
		setThrust(0, 445000);
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
