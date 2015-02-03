package com.decmurphy.spx.engine;

import static com.decmurphy.spx.Globals.g0;

/**
 *
 * @author dmurphy
 */
public class Raptor extends Engine {

  public Raptor() {
	setIsp(321, 363);
	setThrust(6.9*1e6, 8.2*1e6);
	setMdot(getVacThrust() / (getVacIsp() * g0));
  }
}
