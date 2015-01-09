package com.decmurphy.spx.profile;

/**
 *
 * @author dmurphy
 */
public class AS_6Profile extends Profile {
	    
    private static final AS_6Profile instance = new AS_6Profile();

    private AS_6Profile() {};
    
    public static Profile getProfile() {
        return instance;
    }
    
}
