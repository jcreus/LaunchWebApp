package com.decmurphy.spx.profile;

/**
 *
 * @author dmurphy
 */
public class SES_8Profile extends Profile {
	    
    private static final SES_8Profile instance = new SES_8Profile();

    private SES_8Profile() {};
    
    public static Profile getProfile() {
        return instance;
    }
    
}
