package com.decmurphy.spx;

import com.decmurphy.spx.profile.DefaultProfile;
import com.decmurphy.spx.profile.Profile;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Globals {

	public static double dt = 0.001;
	public static double t = 0.0;
	public static final double gravConstant = 6.67384e-11;
	public static final double g0 = 9.797333749;
	public static final double massOfEarth = 5.972e24;
	public static final double radiusOfEarth = 6378137;
	public static final double earthVel = 0;//463.8312116;

	public static String flightCode = "";
	public static String coastMap = "";

	public static double latitude = 28.49;
	public static double longitude = 80.58;

	public static double incl = (90 - latitude) * Math.PI / 180;
	public static double lon = (longitude) * Math.PI / 180;
	
	public static Profile inputVars = new DefaultProfile();

	public static double mod(double a, double b) {
		if (a < 0) {
			a *= -1;
		}
		return a < b ? a : mod(a - b, b);
	}

	public static double round(double value, int places) {
		if (places < 0) {
			throw new IllegalArgumentException();
		}

		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

}
