package com.decmurphy.utils;

import static java.lang.Math.PI;

public class Globals {

	public static double dt = 0.01;

  public static final double gravConstant = 6.67384e-11;
	public static final double massOfEarth = 5.972e24;
	public static final double radiusOfEarth = 6378137;
	public static final double g0 = gravConstant*massOfEarth/(radiusOfEarth*radiusOfEarth);
	public static final double earthAngVel = 2*PI/(24*60*60);
	public static final double earthVel = radiusOfEarth*earthAngVel;

}
