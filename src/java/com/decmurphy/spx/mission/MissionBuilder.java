package com.decmurphy.spx.mission;

import com.decmurphy.spx.payload.Payload;
import com.decmurphy.spx.profile.Profile;
import com.decmurphy.spx.vehicle.LaunchVehicle;

/**
 *
 * @author dmurphy
 */
public class MissionBuilder {
	
	public Mission createMission(LaunchVehicle LV, Payload pl, Profile pr) {
		Mission m = new Mission();
		m.addLaunchVehicle(LV);
		m.addPayload(pl);
		m.addProfile(pr);
		return m;
	}
}
