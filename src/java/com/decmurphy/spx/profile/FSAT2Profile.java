package com.decmurphy.spx.profile;

/**
 *
 * @author dmurphy
 */
public class FSAT2Profile extends Profile {
	    
    private static final FSAT2Profile instance = new FSAT2Profile();

    private FSAT2Profile() {}
    
    public static Profile getProfile() {
			instance.clean();
      return instance;
    }
    
}
