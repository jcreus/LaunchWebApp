package com.decmurphy.spx.launchsite;

import com.decmurphy.spx.util.LaunchSite;
import static java.lang.Math.toRadians;

/**
 *
 * @author declan
 */
public class KSC39A extends RawLaunchSite {

	private static final RawLaunchSite instance = new KSC39A();

	private KSC39A() {
		setName("KSC Pad 39-A");
		setCoordinates(new double[]{toRadians(61.39), toRadians(-80.6)});
		setLaunchSiteType(LaunchSite.K39A);
	}

	public RawLaunchSite get() {
		return instance;
	}
}
