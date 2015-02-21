package com.decmurphy.utils;

import static com.decmurphy.utils.Globals.gravConstant;
import static com.decmurphy.utils.Globals.massOfEarth;
import static com.decmurphy.utils.Globals.radiusOfEarth;
import static java.lang.Math.exp;
import static java.lang.Math.log;
import static java.lang.Math.pow;

/**
 *
 * @author dmurphy
 */
public class Physics {
  
  public static double gravityAtAltitude(double alt) {
	return gravConstant*massOfEarth/pow(alt+radiusOfEarth, 2);
  }

  public static double gravityAtRadius(double radius) {
	if (radius < 0.0) {
	  throw new IllegalArgumentException();
	}
	return gravConstant*massOfEarth/pow(radius, 2);
  }

  public static double densityAtAltitude(double altitude) {
	if ((int) (altitude) < 0.0) {
	  throw new IllegalArgumentException();
	}
	return 1.21147*exp(altitude*-1.12727e-4);
  }

  public static double pressureAtAltitude(double altitude) {
	if ((int) (altitude) < 0.0) {
	  throw new IllegalArgumentException();
	}
	return -517.18*log(0.012833*log(6.0985e28*altitude + 2.0981e28));
  }

}
