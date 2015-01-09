package com.decmurphy.spx.profile;

/**
 *
 * @author dmurphy
 */
public class COTS_1Profile extends Profile {
	    
    private static final COTS_1Profile instance = new COTS_1Profile();

    private COTS_1Profile() {};
    
    public static Profile getProfile() {
        return instance;
    }
    
}
