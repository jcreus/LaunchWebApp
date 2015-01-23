package com.decmurphy.spx.profile;

/**
 *
 * @author dmurphy
 */
public class CRS_2Profile extends Profile {
	    
    private static final CRS_2Profile instance = new CRS_2Profile();

    private CRS_2Profile() {}
    
    public static Profile getProfile() {
			instance.clean();
      return instance;
    }
    
}
