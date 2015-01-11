package com.decmurphy.spx.config;

import static com.decmurphy.spx.Globals.inputVars;
import com.decmurphy.spx.exceptions.PayloadException;
import com.decmurphy.spx.payload.DragonV1;
import com.decmurphy.spx.payload.Payload;
import com.decmurphy.spx.payload.Satellite;

/**
 *
 * @author dmurphy
 */
public class PayloadConfig {

	public PayloadConfig() {
	}

	public static Payload getPayload(String flightCode) throws PayloadException {

		Payload p;
		switch (flightCode) {

			case "F9-1":
			case "COTS-1":
			case "COTS-2":
			case "CRS-1":
			case "CRS-2":
			case "CRS-3":
			case "CRS-4":
			case "CRS-5":
				p = new DragonV1();
				break;
			case "FSAT-2":
			case "DEMO":
			case "TBLZR":
			case "RAT":
			case "RAZ":
			case "CASS":
			case "SES-8":
			case "TH-6":
			case "OG2-1":
			case "AS-8":
			case "AS-6":
				p = new Satellite(inputVars.getPayloadMass());
				break;

			default:
				p = null;
				break;
		}

		if (null == p) {
			throw new PayloadException("No Payload Assigned");
		} else if (p.getMass() < 0) {
			throw new PayloadException("Invalid Payload Mass");
		}

		return p;
	}

}
