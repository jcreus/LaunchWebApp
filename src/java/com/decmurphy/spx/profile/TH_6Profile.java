package com.decmurphy.spx.profile;

/**
 *
 * @author dmurphy
 */
public class TH_6Profile extends Profile {
	    
    private static final TH_6Profile instance = new TH_6Profile();

    private TH_6Profile() {};
    
    public static Profile getProfile() {
			instance.addEvent("attitude1", 400.0).addExtraInfo("stage", 1).addExtraInfo("pitch", -0.5).addExtraInfo("yaw", +0.035);
      return instance;
    }
    
}
