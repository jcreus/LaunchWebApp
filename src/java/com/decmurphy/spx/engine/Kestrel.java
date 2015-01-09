package com.decmurphy.spx.engine;

import static com.decmurphy.spx.physics.Globals.g0;

/**
 *
 * @author dmurphy
 */
public class Kestrel extends Engine
{
	public Kestrel()
	{
		setIsp(0, 317);
		setThrust(0, 31000);
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
