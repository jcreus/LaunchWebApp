package com.decmurphy.spx.profile;

/**
 *
 * @author dmurphy
 */
public class RAZProfile extends Profile {
	    
    private static final RAZProfile instance = new RAZProfile();

    private RAZProfile() {};
    
    public static Profile getProfile() {
        return instance;
    }
    
}
