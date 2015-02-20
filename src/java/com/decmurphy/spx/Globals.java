package com.decmurphy.spx;

import static java.lang.Math.PI;

public class Globals {

	public static double dt = 0.01;
	public static final double gravConstant = 6.67384e-11;
	public static final double g0 = 9.797333749;
	public static final double massOfEarth = 5.972e24;
	public static final double radiusOfEarth = 6378137;
	public static double earthVel = 0.0;
	public static double earthAngVel = 2*PI/(24*60*60);
	public static double simTime = 0.0;

	public static String flightCode = "";

}
