package com.decmurphy.spx.profile;

/**
 *
 * @author dmurphy
 */
public class SES_8Profile extends Profile {
	    
    private static final SES_8Profile instance = new SES_8Profile();

    private SES_8Profile() {};
    
    public static Profile getProfile() {
	  instance.addEvent("adjustCourse", 400.0).addExtraInfo("stage", 1).addExtraInfo("pitch", 0.0);
	  instance.addEvent("adjustCourse", 450.0).addExtraInfo("stage", 1).addExtraInfo("pitch", -0.1);
      return instance;
    }
    
}
