package com.decmurphy.spx;

import com.decmurphy.spx.config.PayloadConfig;
import com.decmurphy.spx.config.LaunchVehicleConfig;
import com.decmurphy.spx.exceptions.PayloadException;
import com.decmurphy.spx.exceptions.LaunchVehicleException;
import static com.decmurphy.spx.Globals.dt;
import static com.decmurphy.spx.Globals.flightCode;
import static com.decmurphy.spx.Globals.mod;
import static com.decmurphy.spx.Globals.t;
import com.decmurphy.spx.config.ProfileConfig;
import com.decmurphy.spx.exceptions.ProfileException;
import com.decmurphy.spx.profile.DefaultProfile;
import com.decmurphy.spx.profile.Profile;
import com.decmurphy.spx.space.Earth;
import com.decmurphy.spx.space.Planet;
import com.decmurphy.spx.vehicle.Falcon1;
import com.decmurphy.spx.payload.Payload;
import com.decmurphy.spx.vehicle.LaunchVehicle;
import com.decmurphy.spx.payload.Satellite;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Launch {

	public static void main(String[] args) {

		String simId = args[0];
		Planet Earth = new Earth(0, 0, 0, simId);

		Payload payload = new Satellite(500);
		LaunchVehicle LV = new Falcon1();
		Profile profile = new DefaultProfile();
		
		try {
			LV = LaunchVehicleConfig.getLaunchVehicle(flightCode);
			payload = PayloadConfig.getPayload(flightCode);
			profile = ProfileConfig.getProfile(flightCode);
		} catch (LaunchVehicleException | PayloadException | ProfileException e) {
			Logger.getLogger(Launch.class.getName()).log(Level.SEVERE, null, e);
		}

		boolean SECO = false;
		LV.setClock(-60.0);
		t = 0.0;

		do {

			/*
			 *	Operator overloading would make this look at a lot nicer. Pity Java doesn't support it.
			 *	Basically what's happening in the 'if' statements here is the equivalent of, for example,
			 *	"if(onBoardClock == 167.0) { LV.MECO() }"
			 *	Even though 'dt' is an even factor of 1.0, it's not very reliable to say "if(onBoardClock == someTime)".
			 *	I'm not sure why.
			 */
			if (Math.abs(LV.clock() - profile.getMEITime()) < 0.5 * dt) {
				LV.firstStageIgnition();
			} else if (Math.abs(LV.clock() - profile.getLaunchTime()) < 0.5 * dt) {
				LV.releaseClamps();
			} else if (Math.abs(LV.clock() - profile.getPitchTime()) < 0.5 * dt) {
				LV.pitchKick();
			}
			/* else if (Math.abs(LV.clock() - profile.getMECOTime()) < 0.5 * dt) {
			 LV.MECO();
			 } else if (Math.abs(LV.clock() - profile.getFSSTime()) < 0.5 * dt) {
			 LV.stageSeparation();
			 } else if (Math.abs(LV.clock() - profile.getSEITime()) < 0.5 * dt) {
			 LV.secondStageIgnition();
			 }

			 if (!SECO && (LV.mStage[1].vel() > 7800 || LV.mStage[1].getPropMass() < 100)) {
			 LV.SECO();
			 dt = 0.1;
			 SECO = true;
			 }
			 */
			LV.leapfrogStep();
			if (mod(t, 5.0) < dt) {
				LV.outputFile(simId);
			}

			t += dt;
			/*
							profile.executeEvents();
							profile.getAttitude();
							profile.leapfrogStep();
							profile.outputFile(simId);
			*/

		} while (profile.clock() < 6000);
	}
}
