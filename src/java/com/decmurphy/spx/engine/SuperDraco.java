package com.decmurphy.spx.engine;

import static com.decmurphy.utils.Globals.g0;

/**
 *
 * @author declan
 */
public class SuperDraco extends Engine {

  public SuperDraco() {
    setIsp(235, 134);
    setThrust(128000, 73000);
    setMdot(getVacThrust() / (getVacIsp() * g0));
  }
}
