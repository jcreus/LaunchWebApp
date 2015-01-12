package com.decmurphy.spx.vehicle;

import com.decmurphy.spx.event.Event;
import com.decmurphy.spx.profile.Profile;

/**
 *
 * @author dmurphy
 */
public abstract class LaunchVehicle {
	
	public LaunchVehicle() {}
	
	public abstract void leapfrogFirstStep();
	public abstract void leapfrogStep();
	public abstract void gravityTurn();
	public abstract void outputFile(String simId);
	public abstract void executeEvent(Event e);
	public abstract boolean reachesOrbitalVelocity();
	public abstract boolean depletesFuel();
	
}
