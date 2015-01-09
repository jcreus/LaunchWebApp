package com.decmurphy.spx.profile;

/**
 *
 * @author dmurphy
 */
public class OG2_1Profile extends Profile {
	    
    private static final OG2_1Profile instance = new OG2_1Profile();

    private OG2_1Profile() {};
    
    public static Profile getProfile() {
        return instance;
    }
    
}
