package com.decmurphy.spx.config;

import com.decmurphy.spx.exceptions.ProfileException;
import com.decmurphy.spx.profile.*;
/**
 *
 * @author dmurphy
 */
public class ProfileConfig {
	
	public ProfileConfig() {
	}
	
	public static Profile getProfile(String flightCode) throws ProfileException {

		switch(flightCode) {
			
			case "FSAT-2": return FSAT2Profile.getProfile();
			case "DEMO": return DEMOProfile.getProfile();
			case "TBLZR": return TBLZRProfile.getProfile();
			case "RAT": return RATProfile.getProfile();
			case "RAZ": return RAZProfile.getProfile();
			case "F9-1": return F9_1Profile.getProfile();
			case "COTS-1": return COTS_1Profile.getProfile();
			case "COTS-2": return COTS_2Profile.getProfile();
			case "CRS-1": return CRS_1Profile.getProfile();
			case "CRS-2": return CRS_2Profile.getProfile();
			case "CASS": return CASSProfile.getProfile();
			case "SES-8": return SES_8Profile.getProfile();
			case "TH-6": return TH_6Profile.getProfile();
			case "CRS-3": return CRS_3Profile.getProfile();
			case "OG2-1": return OG2_1Profile.getProfile();
			case "AS-8": return AS_8Profile.getProfile();
			case "AS-6": return AS_6Profile.getProfile();
			case "CRS-4": return CRS_4Profile.getProfile();
			case "CRS-5": return CRS_5Profile.getProfile();
			default: throw new ProfileException("No valid mission specified: default profile applied");
		}
	}
}
