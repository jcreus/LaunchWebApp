package com.decmurphy.spx.engine;

import static com.decmurphy.spx.physics.Globals.g0;

/**
 *
 * @author dmurphy
 */
public class Merlin1Cv extends Engine
{
	public Merlin1Cv()
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
