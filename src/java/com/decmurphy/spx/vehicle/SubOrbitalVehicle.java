package com.decmurphy.spx.vehicle;

import com.decmurphy.spx.event.Event;
import com.decmurphy.spx.gnc.Navigation;

/**
 *
 * @author dmurphy
 */
public class SubOrbitalVehicle extends RawLaunchVehicle {
  
  public SubOrbitalVehicle() {
    numStages = 1;
		mStage = new Stage[numStages];
		mStage[0] = new Stage("BoosterStage");
		mStage[0].setParent(this);
  }
  
  @Override
  public void leapfrogFirstStep() {
		Navigation.leapfrogFirstStep(mStage[0]);
  	incrementClock();		//TSR/mission clock
  }
  
  @Override
  public void leapfrogStep() {
			
    Navigation.leapfrogStep(mStage[0]);
      
		/*
		 *	I gleaned the '55s' value for starting the gravity turn from /u/Wetmelon's KSP video on /r/spacex
		 */
		if (onBoardClock > gravTurnTime) {
			gravityTurn();
		}
		
		incrementClock();		//TSR/mission clock
  }
  
  @Override
  public void executeEvent(Event e) {
    
    super.executeEvent(e);
		if (e.getName().equalsIgnoreCase("firstStageIgnition")) {
			mStage[(int)e.getValueOf("stage")].setThrottle(1.0);
			System.out.printf("T%+7.2f\t%.32s\n", e.getTime(), "First Stage Ignition");
		}
		else if (e.getName().equalsIgnoreCase("releaseClamps")) {
			System.out.printf("T%+7.2f\t%.32s\n", e.getTime(), "Release Clamps");
			this.clampsReleased = true;
			this.leapfrogFirstStep();
		}
		else if (e.getName().equalsIgnoreCase("pitchKick")) {
			Navigation.pitchKick(mStage[(int)e.getValueOf("stage")], e.getValueOf("pitch"), e.getValueOf("yaw"));
			System.out.printf("T%+7.2f\t%.32s\n", e.getTime(), "Pitch Kick");
		}
		else if (e.getName().equalsIgnoreCase("MECO1")) {
			mStage[(int)e.getValueOf("stage")].setThrottle(0.0);
			System.out.printf("T%+7.2f\t%.32s\n", e.getTime(), "MECO-1");
      mStage[(int)e.getValueOf("stage")].outputFile(getMission().getMissionId(), true);
		}
	}
  
}
