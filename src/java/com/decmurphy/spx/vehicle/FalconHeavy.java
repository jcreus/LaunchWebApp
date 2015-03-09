/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.decmurphy.spx.vehicle;

import com.decmurphy.spx.engine.Merlin1D;
import com.decmurphy.spx.engine.Merlin1DVac;
import com.decmurphy.spx.payload.DragonV1;
import com.decmurphy.spx.payload.RawPayload;
import com.decmurphy.spx.util.LaunchVehicle;

/**
 *
 * @author dmurphy
 */
public class FalconHeavy extends ThreeStageRocket {

	public FalconHeavy(RawPayload payload) {
		this();
		setPayload(payload);
		setLaunchVehicleType(LaunchVehicle.FNH);
	}
	
	public FalconHeavy() {
		
		setPayload(new DragonV1());

		mStage[0].setEngines(18, new Merlin1D());
		mStage[0].setAeroProperties(1.83, 0.3);
		mStage[0].setDryMass(mStage[0].hasLegs() ? 40000 : 36000);
		mStage[0].setFuelCapacity(780000);
		mStage[0].setPropMass(2*mStage[0].getFuelCapacity());

		mStage[1].setEngines(9, new Merlin1D());
		mStage[1].setAeroProperties(1.83, 0.3);
		mStage[1].setDryMass(mStage[0].hasLegs() ? 20000 : 18000);
		mStage[1].setFuelCapacity(390000);
		mStage[1].setPropMass(mStage[1].getFuelCapacity());
    
		mStage[2].setEngines(1, new Merlin1DVac());
		mStage[2].setAeroProperties(1.83, 0.3);
		mStage[2].setDryMass(4900);
		mStage[2].setFuelCapacity(75700);
		mStage[2].setPropMass(mStage[2].getFuelCapacity());

		this.gravTurnTime = 55.0;
	}
  
}
