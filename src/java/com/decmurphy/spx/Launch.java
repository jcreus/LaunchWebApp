package com.decmurphy.spx;

import static com.decmurphy.spx.Globals.dt;
import com.decmurphy.spx.mission.Mission;
import com.decmurphy.spx.space.Earth;
import com.decmurphy.spx.space.Planet;

public class Launch {

	public static void execute(Mission mission, String[] args) {

		Planet Earth = new Earth(0, 0, 0, mission.getMissionId());

		boolean SECO = false;
		mission.setClock(-60.0);

		do {

			mission.executeEvents();
			mission.invokeProfile();
			mission.leapfrogStep();
			mission.outputFile();

			if (!SECO && (mission.LaunchVehicle().reachesOrbitalVelocity() || mission.LaunchVehicle().depletesFuel())) {
				mission.executeOverrideEvent(1, "SECO1");
				SECO = true;
			}

      if(SECO && mission.LaunchVehicle().isLanded()) dt = 0.1;      
      if(mission.LaunchVehicle().failedToReachOrbit()) break;
      if(mission.LaunchVehicle().completedOrbits() >= 2) break;
      
		} while (true);

	}
}
