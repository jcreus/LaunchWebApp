package com.decmurphy.spx.engine;

import static com.decmurphy.spx.physics.Globals.g0;

/**
 *
 * @author dmurphy
 */
public class Merlin1C extends Engine
{
	public Merlin1C()
	{
		setIsp(275, 304);
		setThrust(420000, 480000);
		setMdot(getVacThrust()/(getVacIsp() * g0));
	}

}
