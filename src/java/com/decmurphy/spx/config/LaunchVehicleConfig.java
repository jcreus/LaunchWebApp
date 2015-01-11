package com.decmurphy.spx.config;

import com.decmurphy.spx.exceptions.LaunchVehicleException;
import com.decmurphy.spx.vehicle.Falcon1;
import com.decmurphy.spx.vehicle.Falcon9;
import com.decmurphy.spx.vehicle.Falcon9_1;
import com.decmurphy.spx.vehicle.LaunchVehicle;

/**
 *
 * @author dmurphy
 */
public class LaunchVehicleConfig {

	public LaunchVehicleConfig() {
	}

	public static LaunchVehicle getLaunchVehicle(String flightCode) throws LaunchVehicleException {
		
		switch (flightCode) {
			case "FSAT-2":
			case "DEMO":
			case "TBLZR":
			case "RAT":
			case "RAZ":
				return new Falcon1();
			case "F9-1":
			case "COTS-1":
			case "COTS-2":
			case "CRS-1":
			case "CRS-2":
				return new Falcon9();
			case "CASS":
			case "SES-8":
			case "TH-6":
			case "CRS-3":
			case "OG2-1":
			case "AS-8":
			case "AS-6":
			case "CRS-4":
			case "CRS-5":
				return new Falcon9_1();
			default:
				throw new LaunchVehicleException("No valid mission specified");
		}
	}
}
