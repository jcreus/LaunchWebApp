package com.decmurphy.spx.launchsite;

import com.decmurphy.spx.util.LaunchSite;

/**
 *
 * @author declan
 */
public abstract class RawLaunchSite {
	
	private double inclination;
	private double longitude;
	private String name;
	private LaunchSite ls;
	
	public RawLaunchSite() {}
	
	public double getIncl() {
		return inclination;
	}
	
	public double getLong() {
		return longitude;
	}
	
	public double[] getCoordinates() {
		return new double[]{inclination, longitude};
	}
	
	public String getName() {
		return name;
	}
	
	public void setIncl(double incl) {
		inclination = incl;
	}
	
	public void setLong(double lon) {
		longitude = lon;
	}
	
	public void setCoordinates(double[] coords) {
		inclination = coords[0];
		longitude = coords[1];
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setLaunchSiteType(LaunchSite ls) {
		this.ls = ls;
	}
	
	public LaunchSite getLaunchSiteType() {
		return ls;
	}
	
}
