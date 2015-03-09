package com.decmurphy.spx.vehicle;

import com.decmurphy.spx.engine.Merlin1D;
import com.decmurphy.spx.engine.Merlin1DVac;
import com.decmurphy.spx.payload.DragonV1;
import com.decmurphy.spx.payload.RawPayload;
import com.decmurphy.spx.util.LaunchVehicle;

public class Falcon9_1 extends TwoStageRocket {

	public Falcon9_1(RawPayload payload) {
		this();
		setPayload(payload);
		setLaunchVehicleType(LaunchVehicle.F91);
	}
	
	public Falcon9_1() {
		
		setPayload(new DragonV1());

		mStage[0].setEngines(9, new Merlin1D());
		mStage[0].setAeroProperties(1.83, 0.3);
		mStage[0].setDryMass(mStage[0].hasLegs() ? 20000 : 18000);
		mStage[0].setFuelCapacity(390000);
		mStage[0].setMinimumFuelLimit(0);
		mStage[0].setPropMass(mStage[0].getFuelCapacity());

		mStage[1].setEngines(1, new Merlin1DVac());
		mStage[1].setAeroProperties(1.83, 0.3);
		mStage[1].setDryMass(4900);
		mStage[1].setFuelCapacity(75700);
		mStage[1].setMinimumFuelLimit(300);
		mStage[1].setPropMass(mStage[1].getFuelCapacity());

		this.gravTurnTime = 55.0;
	}
  
}
