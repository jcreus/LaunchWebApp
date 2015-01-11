package com.decmurphy.spx.vehicle;

import static com.decmurphy.spx.Globals.dt;
import com.decmurphy.spx.gnc.Navigation;
import com.decmurphy.spx.payload.Payload;

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
		if (onBoardClock > gravTurnTime)
			gravityTurn();

		TwoStageRocket.onBoardClock += dt;
	}

	@Override
	public void gravityTurn() {
			Navigation.gravityTurn(mStage[0]);
			Navigation.gravityTurn(mStage[1]);
	}

	public void outputFile(String id) {
		mStage[0].outputFile(id);
		if (!beforeSep) {
			mStage[1].outputFile(id);
		}
	}

	public double clock() {
		return onBoardClock;
	}

	public void setClock(double t) {
		onBoardClock = t;
	}

}
