package com.decmurphy.spx.profile;

/**
 *
 * @author dmurphy
 */
public class DSCOVRProfile extends Profile {

	private static final DSCOVRProfile instance = new DSCOVRProfile();

	private DSCOVRProfile() {
	}

	public static Profile getProfile() {
		instance.clean();
		return instance;
	}

}