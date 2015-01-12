package com.decmurphy.spx.profile;

import static com.decmurphy.spx.Globals.t;
import com.decmurphy.spx.event.Event;
import com.decmurphy.spx.payload.Payload;
import com.decmurphy.spx.vehicle.LaunchVehicle;
import java.util.List;

/**
 *
 * @author declan
 */
public abstract class Profile {

	private double mei_time;
	private double launch_time;
	private double pitch_time;
	private double meco_time;
	private double fss_time;
	private double sei_time;
	private double seco_time;
	private double sss_time;

	private double pitch;
	private double yaw;

	private boolean legs;
	private final Payload payload = null;
	private final LaunchVehicle LV = null;
	
	private List<Event> events;

	protected Profile() {}
	
	public List<Event> events() {
		return events;
	}
	
	public void addEvent(String name, double time) {
		events.add(new Event(name, time));
	}
	
	public double getEventTime(String name) {
		for(Event e : events) {
			if(e.getName().equalsIgnoreCase(name))
				return e.getTime();
		}
		return -999;
	}
	
	public void firstStageIgnition() {
		System.out.printf("T%+7.2f\t%.32s\n", clock(), "First Stage Ignition");
	}

	public void releaseClamps() {
		System.out.printf("T%+7.2f\t%.32s\n", clock(), "Release Clamps");
	}

	public void pitchKick() {
		System.out.printf("T%+7.2f\t%.32s\n", clock(), "Pitch Kick");
	}

	public void MECO() {
		System.out.printf("T%+7.2f\t%.32s\n", clock(), "MECO");
	}

	public void stageSeparation() {
		System.out.printf("T%+7.2f\t%.32s\n", clock(), "Stage Separation");
	}

	public void secondStageIgnition() {
		System.out.printf("T%+7.2f\t%.32s\n", clock(), "Second Stage Ignition");
	}

	public void SECO() {
		System.out.printf("T%+7.2f\t%.32s\n", clock(), "SECO");
	}
/*
	protected void leapfrogFirstStep() {
			LV.leapfrogFirstStep();
	}

	protected void leapfrogStep() {
			LV.leapfrogStep();
	}
*/
	//////////////////////////////////////////////////////////////////////////////	

	public void setMEITime(double t) {
		this.mei_time = t;
	}

	public void setLaunchTime(double t) {
		this.launch_time = t;
	}

	public void setPitchTime(double t) {
		this.pitch_time = t;
	}

	public void setMECOTime(double t) {
		this.meco_time = t;
	}

	public void setFSSTime(double t) {
		this.fss_time = t;
	}

	public void setSEITime(double t) {
		this.sei_time = t;
	}

	public void setSECOTime(double t) {
		this.seco_time = t;
	}

	public void setSSSTime(double t) {
		this.sss_time = t;
	}

	public void setPitch(double pitch) {
		this.pitch = pitch;
	}

	public void setYaw(double yaw) {
		this.yaw = yaw;
	}

	public void setLegs(boolean hasLegs) {
		this.legs = hasLegs;
	}

	public void setPayloadMass(double mass) {
		this.payload.setMass(mass);
	}

	public double getMEITime() {
		return this.mei_time;
	}

	public double getLaunchTime() {
		return this.launch_time;
	}

	public double getPitchTime() {
		return this.pitch_time;
	}

	public double getMECOTime() {
		return this.meco_time;
	}

	public double getFSSTime() {
		return this.fss_time;
	}

	public double getSEITime() {
		return this.sei_time;
	}

	public double getSECOTime() {
		return this.seco_time;
	}

	public double getSSSTime() {
		return this.sss_time;
	}

	public double getPitch() {
		return this.pitch;
	}

	public double getYaw() {
		return this.yaw;
	}

	public double getPayloadMass() {
		return this.payload.getMass();
	}

	public boolean hasLegs() {
		return this.legs;
	}

	public double clock() {
		return t;
	}

}
