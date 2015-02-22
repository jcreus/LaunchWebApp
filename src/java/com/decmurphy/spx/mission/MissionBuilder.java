package com.decmurphy.spx.mission;

import com.decmurphy.spx.config.LaunchSiteConfig;
import com.decmurphy.spx.config.LaunchVehicleConfig;
import com.decmurphy.spx.config.PayloadConfig;
import com.decmurphy.spx.exceptions.LaunchSiteException;
import com.decmurphy.spx.exceptions.LaunchVehicleException;
import com.decmurphy.spx.exceptions.PayloadException;
import com.decmurphy.spx.launchsite.LaunchSite;
import com.decmurphy.spx.launchsite.SLC40;
import com.decmurphy.spx.payload.Payload;
import com.decmurphy.spx.payload.Satellite;
import com.decmurphy.spx.profile.Profile;
import com.decmurphy.spx.vehicle.Falcon9_1;
import com.decmurphy.spx.vehicle.LaunchVehicle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dmurphy
 */
public class MissionBuilder {

	public Mission createMission(LaunchVehicle LV, Payload pl, Profile pr, LaunchSite ls) {
		Mission m = new Mission();
		
		m.addLaunchVehicle(LV);
		m.addPayload(pl);
		m.addProfile(pr);
		m.addLaunchSite(ls);
		
		m.LaunchVehicle().setPayload(m.Payload());
		m.LaunchVehicle().setMission(m);
		
		return m;
	}

	public Mission createMission(String code) {

		if(null==code || code.isEmpty())
			throw new IllegalArgumentException("Trying to create Mission with invalid FlightCode");
		
		Payload payload = new Satellite(6000);
		LaunchVehicle LV = new Falcon9_1();
		Profile profile = Profile.getNew();
		LaunchSite launchSite = SLC40.get();

		try {
			LV = LaunchVehicleConfig.getLaunchVehicle(code);
			payload = PayloadConfig.getPayload(code);
			launchSite = LaunchSiteConfig.getLaunchSite(code);
						
		} catch (LaunchVehicleException | PayloadException | LaunchSiteException ex) {
			Logger.getLogger(MissionBuilder.class.getName()).log(Level.SEVERE, null, ex);
		}

		Mission m = new Mission();
		m.addLaunchVehicle(LV);
		m.addPayload(payload);
		m.addProfile(profile);
		
		m.LaunchVehicle().setMission(m);
		m.addLaunchSite(launchSite);
		m.LaunchVehicle().setPayload(m.Payload());

		return m;
	}
}
