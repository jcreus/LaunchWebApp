package com.decmurphy.spx.vehicle;

import static com.decmurphy.spx.Globals.dt;
import com.decmurphy.spx.event.Event;
import static com.decmurphy.spx.gnc.HoverSlam.updateLandingThrottle;
import com.decmurphy.spx.gnc.Navigation;
public abstract class TwoStageRocket extends SSTO {

	public TwoStageRocket() {
    numStages = 2;
		mStage = new Stage[numStages];
		mStage[0] = new Stage("BoosterStage");
		mStage[1] = new Stage("SecondStage");
		mStage[0].setParent(this);
		mStage[1].setParent(this);
	}

	@Override
	public void leapfrogFirstStep() {
		mStage[0].setAdditionalMass(mStage[1].getMass() + payload.getMass());
		Navigation.leapfrogFirstStep(mStage[0]);
		
		incrementClock();		//TSR/mission clock
	}

	@Override
	public void leapfrogStep() {

		if (mStage[0].landingBurnIsUnderway() && onBoardClock%3.0 < dt) {
			mStage[0].setThrottle(updateLandingThrottle(mStage[0]));
		}
			
		if (mStage[0].beforeSep) {
			mStage[0].setAdditionalMass(mStage[1].getMass() + payload.getMass());
			Navigation.leapfrogStep(mStage[0]);
		} else {
			mStage[0].setAdditionalMass(0.0);
			Navigation.leapfrogStep(mStage[0]);

			mStage[1].setAdditionalMass(payload.getMass());
			Navigation.leapfrogStep(mStage[1]);
		}

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
    if (e.getName().equalsIgnoreCase("firstStageSep")) {
			mStage[1].syncWith(mStage[0]);
			mStage[0].beforeSep = false;
			System.out.printf("T%+7.2f\t%.32s\n", e.getTime(), "First Stage Separation");
		}
		else if (e.getName().equalsIgnoreCase("secondStageIgnition")) {
			mStage[(int)e.getValueOf("stage")].setThrottle(1.0);
			mStage[(int)e.getValueOf("stage")].isMoving = true;
			System.out.printf("T%+7.2f\t%.32s\n", e.getTime(), "Second Stage Ignition");
		}
		else if (e.getName().equalsIgnoreCase("boost_start")) {
			mStage[0].setEngines(3);
			mStage[0].setThrottle(1.0);
			mStage[0].setExtraBurnIsUnderway(true);
			System.out.printf("T%+7.2f\t%.32s\n", e.getTime(), "Boostback Start");
		}
		else if (e.getName().equalsIgnoreCase("boost_end")) {
			mStage[0].setThrottle(0.0);
			mStage[0].setExtraBurnIsUnderway(false);
			System.out.printf("T%+7.2f\t%.32s\n", e.getTime(), "Boostback End");
		}
		else if (e.getName().equalsIgnoreCase("entry_start")) {
			mStage[0].setEngines(3);
			mStage[0].setThrottle(1.0);
			mStage[0].setExtraBurnIsUnderway(true);
			System.out.printf("T%+7.2f\t%.32s\n", e.getTime(), "ReEntry Burn Start");
		}
		else if (e.getName().equalsIgnoreCase("entry_end")) {
			mStage[0].setThrottle(0.0);
			mStage[0].setExtraBurnIsUnderway(false);
			System.out.printf("T%+7.2f\t%.32s\n", e.getTime(), "ReEntry Burn End");
		}
		else if (e.getName().equalsIgnoreCase("landing_start")) {
			mStage[0].setEngines(1);
			mStage[0].executeLandingBurn();
			System.out.printf("T%+7.2f\t%.32s\n", e.getTime(), "Landing Burn Start");
		}
		else if (e.getName().equalsIgnoreCase("SECO1")) {
			mStage[(int)e.getValueOf("stage")].setThrottle(0.0);
			System.out.printf("T%+7.2f\t%.32s\n", e.getTime(), "SECO-1");
		}
		else if (e.getName().equalsIgnoreCase("secondStageSep")) {
			System.out.printf("T%+7.2f\t%.32s\n", e.getTime(), "Second Stage Separation");
		}

	}
}