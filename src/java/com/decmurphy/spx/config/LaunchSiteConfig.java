package com.decmurphy.spx.config;

import com.decmurphy.spx.exceptions.LaunchSiteException;
import com.decmurphy.spx.launchsite.LaunchSite;
import com.decmurphy.spx.launchsite.Omelek;
import com.decmurphy.spx.launchsite.SLC40;
import com.decmurphy.spx.launchsite.SLC4E;


/**
 *
 * @author dmurphy
 */
public class LaunchSiteConfig {

	public LaunchSiteConfig() {
	}

	public static LaunchSite getLaunchSite(String flightCode) throws LaunchSiteException {
		
		switch (flightCode) {
			case "FSAT-2":
			case "DEMO":
			case "TBLZR":
			case "RAT":
			case "RAZ":
				return Omelek.get();
			case "F9-1":
			case "COTS-1":
			case "COTS-2":
			case "CRS-1":
			case "CRS-2":
			case "SES-8":
			case "TH-6":
			case "CRS-3":
			case "OG2-1":
			case "AS-8":
			case "AS-6":
			case "CRS-4":
			case "CRS-5":
			case "DSCOVR":
				return SLC40.get();
			case "CASS":
				return SLC4E.get();

			default:
				throw new LaunchSiteException("No valid mission specified");
		}
	}
}
