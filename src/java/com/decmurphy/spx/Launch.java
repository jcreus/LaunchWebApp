package com.decmurphy.spx;

import static com.decmurphy.spx.Globals.dt;
import static com.decmurphy.spx.Globals.t;
import com.decmurphy.spx.mission.Mission;
import com.decmurphy.spx.space.Earth;
import com.decmurphy.spx.space.Planet;

public class Launch {

	public static void main(Mission mission, String[] args) {

		String simId = args[0];
		Planet Earth = new Earth(0, 0, 0, simId);

		boolean SECO = false;
		mission.setClock(-60.0);
		t = 0.0;

		do {

			mission.executeEvents();
			mission.invokeProfile();
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
