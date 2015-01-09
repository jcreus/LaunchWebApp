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
public class TBLZRProfile extends Profile {
	    
    private static final TBLZRProfile instance = new TBLZRProfile();

    private TBLZRProfile() {};
    
    public static Profile getProfile() {
        return instance;
    }
    
}
