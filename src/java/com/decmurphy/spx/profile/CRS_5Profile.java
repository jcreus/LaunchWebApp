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
      return instance;
    }
    
}
