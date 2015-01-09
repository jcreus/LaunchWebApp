package com.decmurphy.spx.config;

import static com.decmurphy.spx.physics.Globals.profile;
import com.decmurphy.spx.vehicle.DragonV1;
import com.decmurphy.spx.vehicle.Payload;
import com.decmurphy.spx.vehicle.Satellite;

/**
 *
 * @author dmurphy
 */
public class PayloadConfig {

	public PayloadConfig() {
	}

	public static Payload getPayload(String flightCode) {

		if (flightCode.startsWith("CRS") || flightCode.startsWith("COTS")) {
			return new DragonV1();
		} else {
			return new Satellite(profile.getPayloadMass());
		}

	}

}
