package com.decmurphy.spx.profile;

/**
 *
 * @author dmurphy
 */
public class CRS_5Profile extends Profile {
	    
    private static final CRS_5Profile instance = new CRS_5Profile();

    private CRS_5Profile() {}
    
    public static Profile getProfile() {
			instance.clean();
			instance.addEvent("adjustCourse", 250.0).addExtraInfo("stage", 1).addExtraInfo("pitch", 0.2);
			instance.addEvent("adjustCourse", 350.0).addExtraInfo("stage", 1).addExtraInfo("pitch", 0.1);
			instance.addEvent("adjustCourse", 450.0).addExtraInfo("stage", 1).addExtraInfo("pitch", 0.0);
      return instance;
    }
    
}
