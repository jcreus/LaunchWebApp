package com.decmurphy.spx.config;

import com.decmurphy.spx.exceptions.LaunchVehicleException;
import com.decmurphy.spx.util.Payload;
import com.decmurphy.spx.vehicle.Falcon1;
import com.decmurphy.spx.vehicle.Falcon9;
import com.decmurphy.spx.vehicle.Falcon9_1;
import com.decmurphy.spx.vehicle.RawLaunchVehicle;

/**
 *
 * @author dmurphy
 */
public class LaunchVehicleConfig {

	public static RawLaunchVehicle getLaunchVehicle(String flightCode) throws LaunchVehicleException {
		
		Payload launch = Payload.getPayloadType(flightCode);
		switch(launch) {
			case FSAT:
			case DEMO:
			case TBZR:
			case RATS:
			case RAZS:
				return new Falcon1();
      case F9F1:
			case COT1:
			case COT2:
			case CRS1:
			case CRS2:
				return new Falcon9();
			case CASS:
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
				return new Falcon9_1();
			default:
				throw new LaunchVehicleException("No valid mission specified");
		}
	}
	
}
