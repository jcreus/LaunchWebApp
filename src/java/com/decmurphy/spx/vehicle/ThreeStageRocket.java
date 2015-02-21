package com.decmurphy.spx.vehicle;

import static com.decmurphy.spx.gnc.HoverSlam.updateLandingThrottle;
import com.decmurphy.spx.gnc.Navigation;
import static com.decmurphy.utils.Globals.dt;

/**
 *
 * @author dmurphy
 */
public class ThreeStageRocket extends TwoStageRocket {
  	
  public ThreeStageRocket() {
		mStage = new Stage[3];
		mStage[0] = new Stage("BoosterStage");
		mStage[1] = new Stage("CoreStage");
		mStage[2] = new Stage("SecondStage");
		mStage[0].setParent(this);
		mStage[1].setParent(this);
		mStage[2].setParent(this);
	}

	@Override
	public void leapfrogFirstStep() {
		mStage[0].setAdditionalMass(mStage[1].getMass() + mStage[2].getMass() + payload.getMass());
		Navigation.leapfrogFirstStep(mStage[0]);
		
		incrementClock();		//TSR/mission clock
	}

	@Override
	public void leapfrogStep() {

		if (mStage[0].landingBurnIsUnderway() && onBoardClock%3.0 < dt) {
			mStage[0].setThrottle(updateLandingThrottle(mStage[0]));
		}
		if (mStage[1].landingBurnIsUnderway() && onBoardClock%3.0 < dt) {
			mStage[1].setThrottle(updateLandingThrottle(mStage[1]));
		}
			
		if (mStage[0].beforeSep) {
			mStage[0].setAdditionalMass(mStage[1].getMass() + mStage[2].getMass() + payload.getMass());
			Navigation.leapfrogStep(mStage[0]);
		} else if(mStage[1].beforeSep) {
			mStage[0].setAdditionalMass(0.0);
			Navigation.leapfrogStep(mStage[0]);
		  
			mStage[1].setAdditionalMass(mStage[2].getMass() + payload.getMass());
			Navigation.leapfrogStep(mStage[1]);
		} else {
			mStage[0].setAdditionalMass(0.0);
			Navigation.leapfrogStep(mStage[0]);
			
			mStage[1].setAdditionalMass(0.0);
			Navigation.leapfrogStep(mStage[1]);

			mStage[2].setAdditionalMass(payload.getMass());
			Navigation.leapfrogStep(mStage[2]);
		}

		/*
		 *	I gleaned the '55s' value for starting the gravity turn from /u/Wetmelon's KSP video on /r/spacex
		 */
		if (onBoardClock > gravTurnTime) {
			gravityTurn();
		}
		
		incrementClock();		//TSR/mission clock
	}

}
