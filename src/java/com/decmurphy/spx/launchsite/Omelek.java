package com.decmurphy.spx.launchsite;

/**
 *
 * @author declan
 */
public class Omelek extends LaunchSite {

	private static final LaunchSite instance = new Omelek();

	private Omelek() {
		setName("Omelek Island, Kwajalein Atoll");
		setCoordinates(new double[]{toRad(90-9.05), toRad(167.74)});
	}

	public static LaunchSite get() {
		return instance;
	}
}
