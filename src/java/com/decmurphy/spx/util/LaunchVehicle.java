package com.decmurphy.spx.util;

/**
 *
 * @author declan
 */
public enum LaunchVehicle {
	
	FN1("Falcon 1"),
	FN9("Falcon 9"),
	F91("Falcon 9 v1.1"),
	FNH("Falcon Heavy");
	
	private final String launchVehicleName;
	
	private LaunchVehicle(String lv) {
		this.launchVehicleName = lv;
	}
	
	public String getLaunchVehicleName() {
		return launchVehicleName;
	}
	
	private LaunchVehicle getLaunchVehicleType(String lv) {
		try {
			return LaunchVehicle.valueOf(lv);
		} catch (Exception e) {
		}
		return null;
	}
	
}
