/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.decmurphy.spx.profile;

/**
 *
 * @author dmurphy
 */
public class F9_1Profile extends Profile {
	    
    private static final F9_1Profile instance = new F9_1Profile();

    private F9_1Profile() {};
    
    public static Profile getProfile() {
        return instance;
    }
    
}
