package com.decmurphy.spx.util;

/**
 *
 * @author declan
 */
public enum LaunchSite {

	OMLK("Omelek Island"),
	LC40("Cape Canaveral AFS SLC-40"),
	LC4E("Vandenburg AFB SLC-4E"),
	K39A("Kennedy Space Center, Pad 39-A"),
	BOCA("Boca Chica Launch Site, Texas");
	
	private final String launchSiteName;
	
	private LaunchSite(String launchSiteName) {
		this.launchSiteName = launchSiteName;
	}
	
	public String getLaunchSiteName() {
		return launchSiteName;
	}

	public static LaunchSite getLaunchSiteType(String launchSite) {
		try {
			return LaunchSite.valueOf(launchSite);
		} catch (Exception e) {
		}
		return null;
  }
	
}
