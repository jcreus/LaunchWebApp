package com.decmurphy.spx.launchsite;

/**
 *
 * @author declan
 */
public class KSC39A extends LaunchSite {

	private static final LaunchSite instance = new KSC39A();

	private KSC39A() {
		setName("KSC Pad 39-A");
		setCoordinates(new double[]{toRad(90-28.61), toRad(80.6)});
	}

	public LaunchSite get() {
		return instance;
	}
}
