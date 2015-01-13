package com.decmurphy.spx.mission;

import com.decmurphy.spx.config.LaunchVehicleConfig;
import com.decmurphy.spx.config.PayloadConfig;
import com.decmurphy.spx.config.ProfileConfig;
import com.decmurphy.spx.exceptions.LaunchVehicleException;
import com.decmurphy.spx.exceptions.PayloadException;
import com.decmurphy.spx.exceptions.ProfileException;
import com.decmurphy.spx.payload.Payload;
import com.decmurphy.spx.payload.Satellite;
import com.decmurphy.spx.profile.DefaultProfile;
import com.decmurphy.spx.profile.Profile;
import com.decmurphy.spx.vehicle.Falcon1;
import com.decmurphy.spx.vehicle.LaunchVehicle;
import java.util.logging.Level;
import java.util.logging.Logger;

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

	public Mission createMission(String code) {

		Payload payload = new Satellite(500);
		LaunchVehicle LV = new Falcon1();
		Profile profile = new DefaultProfile();

		try {
			LV = LaunchVehicleConfig.getLaunchVehicle(code);
			payload = PayloadConfig.getPayload(code);
			profile = ProfileConfig.getProfile(code);
		} catch (LaunchVehicleException | PayloadException | ProfileException ex) {
			Logger.getLogger(MissionBuilder.class.getName()).log(Level.SEVERE, null, ex);
		}

		Mission m = new Mission();
		m.addLaunchVehicle(LV);
		m.addPayload(payload);
		m.addProfile(profile);

		return m;
	}
}
