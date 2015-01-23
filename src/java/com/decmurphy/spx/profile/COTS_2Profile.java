package com.decmurphy.spx.profile;

/**
 *
 * @author dmurphy
 */
public class COTS_2Profile extends Profile {
	    
    private static final COTS_2Profile instance = new COTS_2Profile();

    private COTS_2Profile() {}
    
    public static Profile getProfile() {
			instance.clean();
      return instance;
    }
    
}
