package com.decmurphy.spx.engine;

import static com.decmurphy.spx.physics.Globals.*;

public class Merlin1D extends Engine
{
	public Merlin1D()
	{
		setIsp(282, 311);
		setThrust(650000, 720000);
		setMdot(getVacThrust()/(getVacIsp() * g0));
	}

}
