package com.decmurphy.spx.vehicle;

import static com.decmurphy.spx.Globals.dt;

public abstract class TwoStageRocket extends LaunchVehicle {

	protected Payload payload;
	public Stage[] mStage;
	static double onBoardClock;
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
	public void firstStageIgnition() {
		mStage[0].setThrottle(1.0);
		System.out.printf("T%+7.2f\t%.32s\n", clock(), "First Stage Ignition");
	}

	@Override
	public void releaseClamps() {
		System.out.printf("T%+7.2f\t%.32s\n", clock(), "Release Clamps");
		this.clampsReleased = true;
		this.leapfrogFirstStep();
	}

	@Override
	public void pitchKick() {
		mStage[0].pitchKick();
		System.out.printf("T%+7.2f\t%.32s\n", clock(), "Pitch Kick");
	}

	public void MECO() {
		mStage[0].setThrottle(0.0);
		System.out.printf("T%+7.2f\t%.32s\n", clock(), "MECO");
	}

	public void stageSeparation() {
		mStage[1].syncWith(mStage[0]);
		beforeSep = false;
		System.out.printf("T%+7.2f\t%.32s\n", clock(), "Stage Separation");
	}

	public void secondStageIgnition() {
		mStage[1].setThrottle(1.0);
		mStage[1].isMoving = true;
		System.out.printf("T%+7.2f\t%.32s\n", clock(), "Second Stage Ignition");
	}

	public void SECO() {
		mStage[1].setThrottle(0.0);
		System.out.printf("T%+7.2f\t%.32s\n", clock(), "SECO");
	}

	@Override
	public void leapfrogFirstStep() {
		mStage[0].setMass(mStage[0].getMass() + mStage[1].getMass() + payload.getMass());
		mStage[0].leapfrogFirstStep();
	}

	@Override
	public void leapfrogStep() {
		if (beforeSep) {
			mStage[0].setMass(mStage[0].getMass() + mStage[1].getMass() + payload.getMass());
			mStage[0].leapfrogStep();
		} else {
			mStage[0].setMass(mStage[0].getMass());
			mStage[0].leapfrogStep();

			mStage[1].setMass(mStage[1].getMass() + payload.getMass());
			mStage[1].leapfrogStep();
		}

		/*
		 *	I gleaned the '55s' value for starting the gravity turn from /u/Wetmelon's KSP video on /r/spacex
		 */
		if (onBoardClock > gravTurnTime) {
			gravityTurn();
		}

		TwoStageRocket.onBoardClock += dt;
	}

	@Override
	public void gravityTurn() {
		mStage[0].gravityTurn();
		mStage[1].gravityTurn();
	}

	@Override
	public void outputFile(String id) {
		mStage[0].outputFile(id);
		if (!beforeSep) {
			mStage[1].outputFile(id);
		}
	}

	@Override
	public double clock() {
		return onBoardClock;
	}

	@Override
	public void setClock(double t) {
		onBoardClock = t;
	}

}
