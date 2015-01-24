package com.decmurphy.spx;

import static java.lang.Math.PI;

public class Globals {

	public static double dt = 0.01;
	public static final double gravConstant = 6.67384e-11;
	public static final double g0 = 9.797333749;
	public static final double massOfEarth = 5.972e24;
	public static final double radiusOfEarth = 6378137;
	public static final double earthVel = radiusOfEarth*2*PI/(24*60*60); // ~464 m/s at the equator

	public static String flightCode = "";
	public static String coastMap = "";

	public static double mod(double a, double b) {
		if (a < 0) {
			a *= -1;
		}
		return a < b ? a : mod(a - b, b);
	}
}
