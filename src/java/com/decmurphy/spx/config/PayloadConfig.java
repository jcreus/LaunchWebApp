package com.decmurphy.spx.config;

import com.decmurphy.spx.exceptions.PayloadException;
import com.decmurphy.spx.payload.DragonV1;
import com.decmurphy.spx.payload.RawPayload;
import com.decmurphy.spx.payload.Satellite;
import com.decmurphy.spx.util.Payload;

/**
 *
 * @author dmurphy
 */
public class PayloadConfig {

	public static RawPayload getPayload(String flightCode) throws PayloadException {

		RawPayload p;
		Payload launch = Payload.getPayloadType(flightCode);
		switch(launch) {
			case F9F1:
			case COT1:
			case COT2:
			case CRS1:
			case CRS2:
			case CRS3:
			case CRS4:
			case CRS5:
				p = new DragonV1();
				break;
			case FSAT:
			case DEMO:
			case TBZR:
			case RATS:
			case RAZS:
			case CASS:
			case SES8:
			case THM6:
			case OG21:
			case AST8:
			case AST6:
			case DSCR:
      case EUAB:
			case TRK1:
				p = new Satellite();
				break;
			default:
				p = null;
				break;
		}

		if (null == p)
			throw new PayloadException("No Payload Assigned");
		else if (p.getMass() < 0)
			throw new PayloadException("Invalid Payload Mass");

		return p;
	}

}
