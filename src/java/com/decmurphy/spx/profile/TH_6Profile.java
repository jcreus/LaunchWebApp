package com.decmurphy.spx.profile;

/**
 *
 * @author dmurphy
 */
public class TH_6Profile extends Profile {
	    
    private static final TH_6Profile instance = new TH_6Profile();

    private TH_6Profile() {};
    
    public static Profile getProfile() {
				instance.addEvent("gravityTurn", 55.0);
        return instance;
    }
    
}
