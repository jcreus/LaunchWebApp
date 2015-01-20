package com.decmurphy.spx.profile;

/**
 *
 * @author dmurphy
 */
public class TH_6Profile extends Profile {
	    
    private static final TH_6Profile instance = new TH_6Profile();

    private TH_6Profile() {};
    
    public static Profile getProfile() {
	  instance.addEvent("adjustCourse", 300.0).addExtraInfo("stage", 1).addExtraInfo("pitch", 0.0);
	  instance.addEvent("adjustCourse", 350.0).addExtraInfo("stage", 1).addExtraInfo("pitch", -0.1);  // Aim 0.1 rad below level
	  instance.addEvent("adjustCourse", 400.0).addExtraInfo("stage", 1).addExtraInfo("pitch", -0.2);
      return instance;
    }
    
}
