package com.decmurphy.spx.launchsite;

import com.decmurphy.spx.util.LaunchSite;
import static java.lang.Math.toRadians;

/**
 *
 * @author declan
 */
public class Omelek extends RawLaunchSite {

	private static final RawLaunchSite instance = new Omelek();

	private Omelek() {
		setName("Omelek Island, Kwajalein Atoll");
		setCoordinates(new double[]{toRadians(80.95), toRadians(-167.74)});
		setLaunchSiteType(LaunchSite.OMLK);
	}

	public static RawLaunchSite get() {
		return instance;
	}
}
