package com.decmurphy.spx.vehicle;

import com.decmurphy.spx.event.Event;
import com.decmurphy.spx.launchsite.LaunchSite;
import com.decmurphy.spx.mission.Mission;
import com.decmurphy.spx.payload.Payload;
import com.decmurphy.spx.profile.Profile;

/**
 *
 * @author dmurphy
 */
public interface LaunchVehicle {
	
	public void leapfrogFirstStep();
	public void leapfrogStep();
	public void gravityTurn();
	public void outputFile(String simId);
	public void executeEvent(Event e);
	public void setPayload(Payload pl);
	public boolean reachesOrbitalVelocity();
	public boolean depletesFuel();
	public void invoke(Profile p);
	public void setClock(double t);
	public double clock();
	public void setLegs(boolean legs);
	public int completedOrbits();
	public double alt();
	public void setLaunchSite(LaunchSite ls);
	public void setMission(Mission m);
	public Mission getMission();
  public boolean isLanded();
  public void setLanded(boolean b);
	
}
