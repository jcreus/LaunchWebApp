package com.decmurphy.spx.profile;

/**
 *
 * @author dmurphy
 */
public class CRS_3Profile extends Profile {
	    
    private static final CRS_3Profile instance = new CRS_3Profile();

    private CRS_3Profile() {}
    
    public static Profile getProfile() {
			instance.clean();
      return instance;
    }
    
}
