/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.decmurphy.spx.physics;

/**
 *
 * @author declan
 */
public class Profile {
    
    private static final Profile instance = new Profile();
    
    private double mei_time;
    private double launch_time;
    private double pitch_time;
    private double meco_time;
    private double fss_time;
    private double sei_time;
    private double seco_time;
    private double sss_time;
    
    private double pitch;
    private double yaw;
    
    private Profile() {};
    
    public static Profile getProfile() {
        return instance;
    }
    
    public void setMEITime(double t) {
        instance.mei_time = t;
    }
    
    public void setLaunchTime(double t) {
        instance.launch_time = t;
    }
    
    public void setPitchTime(double t) {
        instance.pitch_time = t;
    }
    
    public void setMECOTime(double t) {
        instance.meco_time = t;
    }
    
    public void setFSSTime(double t) {
        instance.fss_time = t;
    }
    
    public void setSEITime(double t) {
        instance.sei_time = t;
    }
    
    public void setSECOTime(double t) {
        instance.seco_time = t;
    }
    
    public void setSSSTime(double t) {
        instance.sss_time = t;
    }
    
    public void setPitch(double pitch) {
        instance.pitch = pitch;
    }
    
    public void setYaw(double yaw) {
        instance.yaw = yaw;
    }
    
    public double getMEITime() {
        return instance.mei_time;
    }
    
    public double getLaunchTime() {
        return instance.launch_time;
    }
    
    public double getPitchTime() {
        return instance.pitch_time;
    }
    
    public double getMECOTime() {
        return instance.meco_time;
    }
    
    public double getFSSTime() {
        return instance.fss_time;
    }
    
    public double getSEITime() {
        return instance.sei_time;
    }
    
    public double getSECOTime() {
        return instance.seco_time;
    }
    
    public double getSSSTime() {
        return instance.sss_time;
    }
    
    public double getPitch() {
        return instance.pitch;
    }
    
    public double getYaw() {
        return instance.yaw;
    }
    
}
