package com.decmurphy.spx.profile;

/**
 *
 * @author dmurphy
 */
public class RATProfile extends Profile {
	    
    private static final RATProfile instance = new RATProfile();

    private RATProfile() {}
    
    public static Profile getProfile() {
			instance.clean();
      return instance;
    }
    
}
