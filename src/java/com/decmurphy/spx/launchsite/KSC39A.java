package com.decmurphy.spx.launchsite;

import static java.lang.Math.toRadians;

/**
 *
 * @author declan
 */
public class KSC39A extends LaunchSite {

	private static final LaunchSite instance = new KSC39A();

	private KSC39A() {
		setName("KSC Pad 39-A");
		setCoordinates(new double[]{toRadians(61.39), toRadians(-80.6)});
	}

	public LaunchSite get() {
		return instance;
	}
}
