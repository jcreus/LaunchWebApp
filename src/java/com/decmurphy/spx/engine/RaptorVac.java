package com.decmurphy.spx.engine;

import static com.decmurphy.spx.Globals.g0;

/**
 *
 * @author dmurphy
 */
public class RaptorVac extends Engine {

  public RaptorVac() {
	setIsp(0, 380);
	setThrust(0, 0);
	setMdot(getVacThrust() / (getVacIsp() * g0));
  }

  @Override
  public double getThrustAtAltitude(double altitude) {
	return this.getIspAtAltitude(altitude) * this.getMdot() * g0;
  }

  @Override
  public double getIspAtAltitude(double altitude) {
	return this.getVacIsp();
  }
}
