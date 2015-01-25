package com.decmurphy.spx.mission;

import static com.decmurphy.spx.Globals.dt;
import static com.decmurphy.spx.Globals.mod;
import com.decmurphy.spx.event.Event;
import com.decmurphy.spx.launchsite.LaunchSite;
import com.decmurphy.spx.payload.Payload;
import com.decmurphy.spx.profile.Profile;
import com.decmurphy.spx.vehicle.LaunchVehicle;
import static java.lang.Math.abs;

/**
 *
 * @author dmurphy
 */
public class Mission {

	private LaunchVehicle LV;
	private Payload payload;
	private Profile profile;
	private LaunchSite LS;

	public void addLaunchVehicle(LaunchVehicle LV) {
		this.LV = LV;
	}

	public void addPayload(Payload payload) {
		this.payload = payload;
	}

	public void addProfile(Profile profile) {
		this.profile = profile;
	}
	
	public void addLaunchSite(LaunchSite launchSite) {
		this.LS = launchSite;
		LaunchVehicle().setLaunchSite(launchSite);
	}

	public void setClock(double t) {
		LV.setClock(t);
	}
	
	public LaunchVehicle LaunchVehicle() {
		return LV;
	}

	public Payload Payload() {
		return payload;
	}

	public Profile Profile() {
		return profile;
	}
	
	public LaunchSite LaunchSite() {
		return LS;
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

	public void outputFile(String simId) {
		if ((mod(this.clock(), 5.0) < dt)) {
			this.LV.outputFile(simId);
		}
	}

}
