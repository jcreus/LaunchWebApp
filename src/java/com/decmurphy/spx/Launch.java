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
import com.decmurphy.spx.mission.Mission;
import com.decmurphy.spx.mission.MissionBuilder;
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

		MissionBuilder mb = new MissionBuilder();
		Mission mission = mb.createMission(LV, payload, profile);

		boolean SECO = false;
		mission.setClock(-60.0);
		t = 0.0;

		do {

			mission.executeEvents();
			mission.getAttitude();
			mission.leapfrogStep();
			mission.outputFile(simId);

			if(!SECO && (mission.LaunchVehicle().reachesOrbitalVelocity() || mission.LaunchVehicle().depletesFuel())) {
				mission.executeEvent("SECO");
				dt = 0.1;
				SECO = true;
			}

			t += dt;

		} while (mission.clock() < 6000);
	}
}
