package com.decmurphy.spx.profile;

/**
 *
 * @author dmurphy
 */
public class TBLZRProfile extends Profile {

	private static final TBLZRProfile instance = new TBLZRProfile();

	private TBLZRProfile() {
	}

	public static Profile getProfile() {
		instance.clean();
		return instance;
	}

}
