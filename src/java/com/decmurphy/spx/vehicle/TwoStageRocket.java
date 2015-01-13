package com.decmurphy.spx.vehicle;

import com.decmurphy.spx.event.Event;
import com.decmurphy.spx.gnc.Navigation;
import com.decmurphy.spx.payload.Payload;
import com.decmurphy.spx.profile.Profile;
import static java.lang.Math.PI;
import static java.lang.Math.atan2;
import static java.lang.Math.sqrt;

public abstract class TwoStageRocket extends LaunchVehicle {

	protected Payload payload;
	public Stage[] mStage;
	static double onBoardClock;
	protected double pitchKickTime;
	protected double gravTurnTime;
	boolean clampsReleased = false;
	boolean beforeSep = false;

	public TwoStageRocket() {
		mStage = new Stage[2];
		mStage[0] = new Stage("BoosterStage");
		mStage[1] = new Stage("SecondStage");
	}

	protected void setCoordinates(double cLat, double cLong) {
		mStage[0].setCoordinates(cLat, cLong);
		mStage[1].setCoordinates(cLat, cLong);
	}

	@Override
	public void leapfrogFirstStep() {
		mStage[0].setMass(mStage[0].getMass() + mStage[1].getMass() + payload.getMass());
		Navigation.leapfrogFirstStep(mStage[0]);
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
	}

	@Override
	public void invoke(Profile p) {
		if (onBoardClock > gravTurnTime) {
			mStage[0].gamma[0] = PI - mStage[0].alpha[0];
			mStage[0].gamma[1] = PI + mStage[0].alpha[1];
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
			mStage[0].setThrottle(1.0);
			System.out.printf("T%+7.2f\t%.32s\n", e.getTime(), "First Stage Ignition");
		}

		if (e.getName().equalsIgnoreCase("releaseClamps")) {
			System.out.printf("T%+7.2f\t%.32s\n", e.getTime(), "Release Clamps");
			this.clampsReleased = true;
			this.leapfrogFirstStep();
		}

		if (e.getName().equalsIgnoreCase("pitchKick")) {
			mStage[0].pitchKick();
			System.out.printf("T%+7.2f\t%.32s\n", e.getTime(), "Pitch Kick");
		}

		if (e.getName().equalsIgnoreCase("MECO")) {
			mStage[0].setThrottle(0.0);
			System.out.printf("T%+7.2f\t%.32s\n", e.getTime(), "MECO");
		}

		if (e.getName().equalsIgnoreCase("stageSeparation")) {
			mStage[1].syncWith(mStage[0]);
			beforeSep = false;
			System.out.printf("T%+7.2f\t%.32s\n", e.getTime(), "Stage Separation");
		}

		if (e.getName().equalsIgnoreCase("secondStageIgnition")) {
			mStage[1].setThrottle(1.0);
			mStage[1].isMoving = true;
			System.out.printf("T%+7.2f\t%.32s\n", e.getTime(), "Second Stage Ignition");
		}

		if (e.getName().equalsIgnoreCase("SECO")) {
			mStage[1].setThrottle(0.0);
			System.out.printf("T%+7.2f\t%.32s\n", e.getTime(), "SECO");
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
}
