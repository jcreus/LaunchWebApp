package com.decmurphy.spx.profile;

/**
 *
 * @author dmurphy
 */
public class DEMOProfile extends Profile {
	    
    private static final DEMOProfile instance = new DEMOProfile();

    private DEMOProfile() {};
    
    public static Profile getProfile() {
        return instance;
    }
    
}
