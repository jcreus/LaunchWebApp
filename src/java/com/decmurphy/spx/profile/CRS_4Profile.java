package com.decmurphy.spx.profile;

/**
 *
 * @author dmurphy
 */
public class CRS_4Profile extends Profile {
	    
    private static final CRS_4Profile instance = new CRS_4Profile();

    private CRS_4Profile() {};
    
    public static Profile getProfile() {
        return instance;
    }
    
}
