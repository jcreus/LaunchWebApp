package com.decmurphy.spx.engine;

import static com.decmurphy.spx.Globals.*;
import com.decmurphy.spx.gnc.Navigation;

public abstract class Engine {
	
	private double mdot;
	private double seaLevelIsp, vacIsp;
	private double seaLevelThrust, vacThrust;

	public Engine() {}

	public void setMdot(double mdot) {
		this.mdot = mdot;
	}
	
	public void setIsp(double seaLevelIsp, double vacIsp)	{
		this.seaLevelIsp = seaLevelIsp;
		this.vacIsp = vacIsp;
	}

	public void setThrust(double seaLevelThrust, double vacThrust) {
		this.seaLevelThrust = seaLevelThrust;
		this.vacThrust = vacThrust;
	}

	public double getThrustAtAltitude(double altitude) {
		return this.getIspAtAltitude(altitude)*mdot*g0;
	}

	public double getIspAtAltitude(double altitude)	{
		return this.getSeaLevelIsp()
						+ (1.0/Navigation.pressureAtAltitude(0))*(Navigation.pressureAtAltitude(0)
						- Navigation.pressureAtAltitude(altitude*1e-3)) * (this.getVacIsp() - this.getSeaLevelIsp());
	}

	public double getMdot()	{
		return mdot;
	}

	public double getSeaLevelIsp() {
		return seaLevelIsp;
	}

	public double getVacIsp()	{
		return vacIsp;
	}

	public double getSeaLevelThrust()	{
		return seaLevelThrust;
	}

	public double getVacThrust() {
		return vacThrust;
	}

}
