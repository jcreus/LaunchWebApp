package com.decmurphy.spx.profile;

/**
 *
 * @author dmurphy
 */
public class CRS_4Profile extends Profile {

	private static final CRS_4Profile instance = new CRS_4Profile();

	private CRS_4Profile() {}

	public static Profile getProfile() {
		instance.clean();
		instance.addEvent("attitude1", 310.0).addExtraInfo("stage", 1).addExtraInfo("pitch", -0.5).addExtraInfo("yaw", +0.035);
		instance.addEvent("thrust", 365.0).addExtraInfo("stage", 1).addExtraInfo("throttle", 80);	// percent
		instance.addEvent("attitude2", 510.0).addExtraInfo("stage", 1).addExtraInfo("pitch", -0.3).addExtraInfo("yaw", +0.004);
		return instance;
	}

}
