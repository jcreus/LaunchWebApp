package com.decmurphy.spx.vehicle;

import static com.decmurphy.spx.Globals.dt;
import com.decmurphy.spx.event.Event;
import com.decmurphy.spx.gnc.Navigation;
import com.decmurphy.spx.payload.Payload;
import com.decmurphy.spx.profile.Profile;

public abstract class TwoStageRocket implements LaunchVehicle {

	protected Payload payload;
	public Stage[] mStage;
	protected double onBoardClock;
	protected double pitchKickTime;
	protected double gravTurnTime;
	boolean clampsReleased = false;
	boolean beforeSep = false;

	public TwoStageRocket() {
		mStage = new Stage[2];
		mStage[0] = new Stage("BoosterStage");
		mStage[1] = new Stage("SecondStage");
	}
	
	@Override
	public void setPayload(Payload pl) {
		payload = pl;
	}
	
	@Override
	public void setClock(double t) {
		onBoardClock = t;
	}
	
	@Override
	public double clock() {
		return onBoardClock;
	}

	protected void setCoordinates(double cLat, double cLong) {
		mStage[0].setCoordinates(cLat, cLong);
		mStage[1].setCoordinates(cLat, cLong);
	}
	
	@Override
	public int completedOrbits() {
		return mStage[1].completedOrbits();
	}

	@Override
	public void leapfrogFirstStep() {
		mStage[0].setMass(mStage[0].getMass() + mStage[1].getMass() + payload.getMass());
		Navigation.leapfrogFirstStep(mStage[0]);
		
		onBoardClock += dt;		//TSR/mission clock
	}

	@Override
	public void leapfrogStep() {
		if (beforeSep) {
			mStage[0].setMass(mStage[0].getMass() + mStage[1].getMass() + payload.getMass());
			Navigation.leapfrogStep(mStage[0]);
		} else {
			mStage[0].setMass(mStage[0].getMass());
			Navigation.leapfrogStep(mStage[0]);

			mStage[1].setMass(mStage[1].getMass() + payload.getMass());
			Navigation.leapfrogStep(mStage[1]);
		}

		/*
		 *	I gleaned the '55s' value for starting the gravity turn from /u/Wetmelon's KSP video on /r/spacex
		 */
		if (onBoardClock > gravTurnTime) {
			gravityTurn();
		}
		
		onBoardClock += dt;		//TSR/mission clock
	}

	@Override
	public void invoke(Profile p) {
		/*
			Want to make sure any attitude changes keep effect permanently - not just for the moment they are initiated.
			By 'permanently', I of course mean until it's replaced by the next attitude change.
		*/
		Event event;
		if((event = p.getEvent(onBoardClock)) != null) {
			executeEvent(event);
		}
	}

	@Override
	public void gravityTurn() {
		Navigation.gravityTurn(mStage[0]);
		if (!beforeSep) {
			Navigation.gravityTurn(mStage[1]);
		}
	}

	@Override
	public void outputFile(String id) {
		mStage[0].outputFile(id);
		if (!beforeSep) {
			mStage[1].outputFile(id);
		}
	}

	@Override
	public void executeEvent(Event e) {

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
		}
		else if (e.getName().equalsIgnoreCase("firstStageSep")) {
			mStage[1].syncWith(mStage[0]);
			beforeSep = false;
			System.out.printf("T%+7.2f\t%.32s\n", e.getTime(), "First Stage Separation");
		}
		else if (e.getName().equalsIgnoreCase("secondStageIgnition")) {
			mStage[(int)e.getValueOf("stage")].setThrottle(1.0);
			mStage[(int)e.getValueOf("stage")].isMoving = true;
			System.out.printf("T%+7.2f\t%.32s\n", e.getTime(), "Second Stage Ignition");
		}
		else if (e.getName().equalsIgnoreCase("SECO1")) {
			mStage[(int)e.getValueOf("stage")].setThrottle(0.0);
			System.out.printf("T%+7.2f\t%.32s\n", e.getTime(), "SECO-1");
		}
		else if (e.getName().equalsIgnoreCase("secondStageSep")) {
			System.out.printf("T%+7.2f\t%.32s\n", e.getTime(), "Second Stage Separation");
		}
		
		if(e.getName().startsWith("attitude")) {
			Navigation.pitchKick(mStage[(int)e.getValueOf("stage")], e.getValueOf("pitch"), e.getValueOf("yaw"));
		}
		else if(e.getName().startsWith("thrust")) {
			mStage[(int)e.getValueOf("stage")].setThrottle(e.getValueOf("throttle"));
		}
	}

	@Override
	public boolean reachesOrbitalVelocity() {
		if (mStage[1].vel() > 7800) {
			return true;
		}
		return false;
	}

	@Override
	public boolean depletesFuel() {
		if (mStage[1].getPropMass() < 100) {
			return true;
		}
		return false;
	}
	
	@Override
	public void setLegs(boolean legs) {
		mStage[0].setLegs(legs);
	}
}
