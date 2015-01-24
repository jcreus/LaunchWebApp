package com.decmurphy.spx.launchsite;

/**
 *
 * @author declan
 */
public class SLC40 extends LaunchSite {

	private static final LaunchSite instance = new SLC40();
	private SLC40() {
		setName("CCAFS SLC-40");
		setCoordinates(new double[]{toRad(90-28.56),toRad(80.57)});
	}
	
	public static LaunchSite get() {
		return instance;
	}
}
