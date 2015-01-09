package com.decmurphy.spx.vehicle;

/**
 *
 * @author dmurphy
 */
public abstract class LaunchVehicle {
	
	public LaunchVehicle() {}
    public abstract void firstStageIgnition();	
    public abstract void releaseClamps();
	public abstract void pitchKick();
    public abstract void leapfrogFirstStep();
    public abstract void leapfrogStep();
    public abstract void gravityTurn();
    public abstract void outputFile(String id);
    public abstract double clock();
    public abstract void setClock(double t);

}
