package com.decmurphy.spx.launchsite;

/**
 *
 * @author declan
 */
public class SLC4E extends LaunchSite {

	private static final LaunchSite instance = new SLC4E();
	private SLC4E() {
		setName("Vandenburg SLC-4E");
		setCoordinates(new double[]{toRad(90-34.63),toRad(120.61)});
	}
	
	public static LaunchSite get() {
		return instance;
	}
}
