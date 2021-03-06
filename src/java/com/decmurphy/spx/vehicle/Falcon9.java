package com.decmurphy.spx.vehicle;

import com.decmurphy.spx.engine.Merlin1C;
import com.decmurphy.spx.engine.Merlin1CVac;
import com.decmurphy.spx.payload.DragonV1;
import com.decmurphy.spx.payload.RawPayload;

/**
 *
 * @author dmurphy
 */
public class Falcon9 extends TwoStageRocket {

	public Falcon9(RawPayload payload) {
		this();
		setPayload(payload);
	}
	
	public Falcon9()	{

		setPayload(new DragonV1());

		mStage[0].setEngines(9, new Merlin1C());
		mStage[0].setAeroProperties(1.83, 0.3);
		mStage[0].setDryMass(15000);
		mStage[0].setFuelCapacity(30000);
		mStage[0].setPropMass(mStage[0].getFuelCapacity());

		mStage[1].setEngines(1, new Merlin1CVac());
		mStage[1].setAeroProperties(1.83, 0.3);
		mStage[1].setDryMass(4000);
		mStage[1].setFuelCapacity(65000);
		mStage[1].setPropMass(mStage[1].getFuelCapacity());

		this.gravTurnTime = 55.0;
	}
}