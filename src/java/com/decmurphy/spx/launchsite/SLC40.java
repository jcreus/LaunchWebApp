package com.decmurphy.spx.launchsite;

import static java.lang.Math.toRadians;

/**
 *
 * @author declan
 */
public class SLC40 extends RawLaunchSite {

	private static final RawLaunchSite instance = new SLC40();
	private SLC40() {
		setName("CCAFS SLC-40");
    // See http://en.wikipedia.org/wiki/Spherical_coordinate_system#mediaviewer/File:3D_Spherical.svg
    // First coordinate is theta, second is phi. phi=0 is the prime meridian
		setCoordinates(new double[]{toRadians(61.44),toRadians(-80.57)});
	}
	
	public static RawLaunchSite get() {
		return instance;
	}
}
