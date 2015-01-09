package com.decmurphy.spx.profile;

/**
 *
 * @author dmurphy
 */
public class AS_8Profile extends Profile {
	    
    private static final AS_8Profile instance = new AS_8Profile();

    private AS_8Profile() {};
    
    public static Profile getProfile() {
        return instance;
    }
    
}
