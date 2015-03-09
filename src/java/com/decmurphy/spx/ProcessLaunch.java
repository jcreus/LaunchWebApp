package com.decmurphy.spx;

import com.decmurphy.spx.mission.Mission;
import com.decmurphy.spx.space.Earth;
import com.decmurphy.spx.space.Planet;
import static com.decmurphy.utils.Globals.dt;

public class ProcessLaunch {

	public static void execute(Mission mission, String[] args) {

		Planet Earth = new Earth(0, 0, 0, mission.getMissionId());

		boolean SECO = false;
		mission.setClock(-60.0);

		do {

			mission.executeEvents();
			mission.invokeProfile();
			mission.leapfrogStep();
			mission.outputFile();

			if (!SECO && (mission.getLaunchVehicle().reachesOrbitalVelocity() || mission.getLaunchVehicle().depletesFuel())) {
				mission.executeOverrideEvent(1, "SECO1");
				SECO = true;
			}

      if(SECO && mission.getLaunchVehicle().isLanded()) dt = 0.1;      
      if(mission.getLaunchVehicle().failedToReachOrbit()) break;
      if(mission.getLaunchVehicle().completedOrbits() >= 1) break;
      if(mission.clock() > 6000) break;
      
		} while (true);

	}
}
