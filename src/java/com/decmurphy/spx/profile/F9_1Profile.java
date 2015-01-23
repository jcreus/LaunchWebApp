package com.decmurphy.spx.profile;

/**
 *
 * @author dmurphy
 */
public class F9_1Profile extends Profile {
	    
    private static final F9_1Profile instance = new F9_1Profile();

    private F9_1Profile() {}
    
    public static Profile getProfile() {
			instance.clean();
      return instance;
    }
    
}
