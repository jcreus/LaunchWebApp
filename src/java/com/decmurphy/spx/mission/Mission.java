package com.decmurphy.spx.mission;

import com.decmurphy.spx.event.Event;
import com.decmurphy.spx.launchsite.RawLaunchSite;
import com.decmurphy.spx.payload.RawPayload;
import com.decmurphy.spx.profile.Profile;
import com.decmurphy.spx.vehicle.LaunchVehicle;
import static com.decmurphy.utils.Globals.dt;
import static java.lang.Math.abs;

/**
 *
 * @author dmurphy
 */
public class Mission {

	private LaunchVehicle LV;
	private RawPayload payload;
	private Profile profile;
	private RawLaunchSite LS;
  private String missionId; 

	public void addLaunchVehicle(LaunchVehicle LV) {
		this.LV = LV;
	}

	public void addPayload(RawPayload payload) {
		this.payload = payload;
	}

	public void addProfile(Profile profile) {
		this.profile = profile;
	}
	
	public void addLaunchSite(RawLaunchSite launchSite) {
		this.LS = launchSite;
		LaunchVehicle().setLaunchSite(launchSite);
	}
  
  public void setMissionId(String id) {
    this.missionId = id;
  }

	public void setClock(double t) {
		LV.setClock(t);
	}
	
	public LaunchVehicle LaunchVehicle() {
		return LV;
	}

	public RawPayload Payload() {
		return payload;
	}

	public Profile Profile() {
		return profile;
	}
	
	public RawLaunchSite LaunchSite() {
		return LS;
	}
  
  public String getMissionId() {
    return missionId;
  }

	public double clock() {
		return LV.clock();
	}

	public void executeEvents() {
		for (Event e : this.profile.events()) {
			if (abs(this.clock() - e.getTime()) < dt / 2)
				LV.executeEvent(e);
		}
	}
	
	public void executeEvent(String s) {
		for(Event e : profile.events()) {
			if(e.getName().equalsIgnoreCase(s))
				LV.executeEvent(e);
		}
	}
	
	public void executeOverrideEvent(int stage, String s) {
		LV.executeEvent(new Event(s, clock()).addExtraInfo("stage", stage));
	}

	public void invokeProfile() {
		LV.invoke(profile);
	}

	public void leapfrogStep() {
		LV.leapfrogStep();
	}

	public void outputFile() {
		if (abs(clock())%5.0 < dt) {
			this.LV.outputFile();
		}
	}

}
