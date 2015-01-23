package com.decmurphy.spx.profile;

/**
 *
 * @author dmurphy
 */
public class DefaultProfile extends Profile {
	    
    private static final DefaultProfile instance = new DefaultProfile();

    public DefaultProfile() {}
    
    public static Profile getProfile() {
			instance.clean();
      return instance;
    }
    
}
