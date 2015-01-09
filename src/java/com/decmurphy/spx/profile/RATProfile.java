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
public class RATProfile extends Profile {
	    
    private static final RATProfile instance = new RATProfile();

    private RATProfile() {};
    
    public static Profile getProfile() {
        return instance;
    }
    
}
