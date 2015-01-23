package com.decmurphy.spx.profile;

/**
 *
 * @author dmurphy
 */
public class CASSProfile extends Profile {
	    
    private static final CASSProfile instance = new CASSProfile();

    private CASSProfile() {}
    
    public static Profile getProfile() {
			instance.clean();
      return instance;
    }
    
}
