package com.decmurphy.spx.profile;

/**
 *
 * @author dmurphy
 */
public class CRS_1Profile extends Profile {
	    
    private static final CRS_1Profile instance = new CRS_1Profile();

    private CRS_1Profile() {};
    
    public static Profile getProfile() {
        return instance;
    }
    
}
