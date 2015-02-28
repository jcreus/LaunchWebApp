package com.decmurphy.spx.launchsite;

import static java.lang.Math.toRadians;

/**
 *
 * @author declan
 */
public class SLC4E extends RawLaunchSite {

	private static final RawLaunchSite instance = new SLC4E();
	private SLC4E() {
		setName("Vandenburg SLC-4E");
		setCoordinates(new double[]{toRadians(55.37),toRadians(-120.61)});
	}
	
	public static RawLaunchSite get() {
		return instance;
	}
}
