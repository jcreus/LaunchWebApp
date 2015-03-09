package com.decmurphy.spx.mission;

import com.decmurphy.spx.config.LaunchSiteConfig;
import com.decmurphy.spx.config.LaunchVehicleConfig;
import com.decmurphy.spx.config.PayloadConfig;
import com.decmurphy.spx.exceptions.LaunchSiteException;
import com.decmurphy.spx.exceptions.LaunchVehicleException;
import com.decmurphy.spx.exceptions.PayloadException;
import com.decmurphy.spx.launchsite.RawLaunchSite;
import com.decmurphy.spx.launchsite.SLC40;
import com.decmurphy.spx.payload.RawPayload;
import com.decmurphy.spx.payload.Satellite;
import com.decmurphy.spx.profile.Profile;
import com.decmurphy.spx.vehicle.Falcon9_1;
import com.decmurphy.spx.vehicle.RawLaunchVehicle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dmurphy
 */
public class MissionBuilder {

	public Mission createMission(RawLaunchVehicle LV, RawPayload pl, RawLaunchSite ls) {
		Mission m = new Mission();
		
		m.addLaunchVehicle(LV);
		m.addPayload(pl);
		m.addLaunchSite(ls);
		
		m.getLaunchVehicle().setPayload(m.getPayload());
		m.getLaunchVehicle().setMission(m);
		
		return m;
	}

	public Mission createMission(String code) {
    
    RawPayload payload = null;
    RawLaunchVehicle LV = null;
    RawLaunchSite launchSite = null;

    if (null == code || code.isEmpty()) {
      
      payload = new Satellite(6000);
      LV = new Falcon9_1();
      launchSite = SLC40.get();
      
    } else {
      
      try {
        LV = LaunchVehicleConfig.getLaunchVehicle(code);
        payload = PayloadConfig.getPayload(code);
        launchSite = LaunchSiteConfig.getLaunchSite(code);
        
      } catch (LaunchVehicleException | PayloadException | LaunchSiteException ex) {
        Logger.getLogger(MissionBuilder.class.getName()).log(Level.SEVERE, null, ex);
      }
      
    }

		Mission m = new Mission();
		m.addLaunchVehicle(LV);
		m.addPayload(payload);
		
		m.getLaunchVehicle().setMission(m);
		m.addLaunchSite(launchSite);
		m.getLaunchVehicle().setPayload(m.getPayload());

		return m;
	}
}
