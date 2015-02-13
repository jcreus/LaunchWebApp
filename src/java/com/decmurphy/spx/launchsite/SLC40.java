package com.decmurphy.spx.launchsite;

/**
 *
 * @author declan
 */
public class SLC40 extends LaunchSite {

	private static final LaunchSite instance = new SLC40();
	private SLC40() {
		setName("CCAFS SLC-40");
    // See http://en.wikipedia.org/wiki/Spherical_coordinate_system#mediaviewer/File:3D_Spherical.svg
    // First coordinate is theta, second is phi. phi=0 is the prime meridian
		setCoordinates(new double[]{toRad(61.44),toRad(-80.57)});
	}
	
	public static LaunchSite get() {
		return instance;
	}
}
