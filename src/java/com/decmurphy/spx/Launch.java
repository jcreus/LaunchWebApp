package com.decmurphy.spx;

import static com.decmurphy.spx.Globals.dt;
import com.decmurphy.spx.mission.Mission;
import com.decmurphy.spx.space.Earth;
import com.decmurphy.spx.space.Planet;

public class Launch {

	public static void execute(Mission mission, String[] args) {

		String simId = args[0];
		Planet Earth = new Earth(0, 0, 0, simId);

		boolean SECO = false;
		mission.setClock(-60.0);

		do {

			mission.executeEvents();
			mission.invokeProfile();
			mission.leapfrogStep();
			mission.outputFile(simId);

			if (!SECO && (mission.LaunchVehicle().reachesOrbitalVelocity() || mission.LaunchVehicle().depletesFuel())) {
				mission.executeOverrideEvent(1, "SECO1");
				SECO = true;
			}

      if(SECO && mission.LaunchVehicle().isLanded())
        dt = 0.1;
      
		} while (mission.LaunchVehicle().completedOrbits() < 1 && (!SECO || mission.LaunchVehicle().alt() > 0));

	}
}
