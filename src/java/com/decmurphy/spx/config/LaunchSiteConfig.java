package com.decmurphy.spx.config;

import com.decmurphy.spx.exceptions.LaunchSiteException;
import com.decmurphy.spx.launchsite.RawLaunchSite;
import com.decmurphy.spx.launchsite.Omelek;
import com.decmurphy.spx.launchsite.SLC40;
import com.decmurphy.spx.launchsite.SLC4E;
import com.decmurphy.spx.util.Payload;


/**
 *
 * @author dmurphy
 */
public class LaunchSiteConfig {

	public static RawLaunchSite getLaunchSite(String flightCode) throws LaunchSiteException {
		
		Payload launch = Payload.getPayloadType(flightCode);
		switch(launch) {
			case FSAT:
			case DEMO:
			case TBZR:
			case RATS:
			case RAZS:
				return Omelek.get();
      case F9F1:
			case COT1:
			case COT2:
			case CRS1:
			case CRS2:
			case SES8:
			case THM6:
			case CRS3:
			case OG21:
			case AST8:
			case AST6:
			case CRS4:
			case CRS5:
			case DSCR:
      case EUAB:
      case TRK1:
				return SLC40.get();
			case CASS:
				return SLC4E.get();
			default:
				throw new LaunchSiteException("No valid mission specified");
		}
	}
	
}
