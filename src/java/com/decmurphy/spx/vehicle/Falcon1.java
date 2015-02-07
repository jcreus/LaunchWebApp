package com.decmurphy.spx.vehicle;

import com.decmurphy.spx.engine.Kestrel;
import com.decmurphy.spx.engine.Merlin1C;
import com.decmurphy.spx.payload.Payload;
import com.decmurphy.spx.payload.Satellite;

/**
 *
 * @author dmurphy
 */
public class Falcon1 extends TwoStageRocket {

	public Falcon1(Payload payload) {
		this();
		setPayload(payload);
	}
	
	public Falcon1() {
		
		setPayload(new Satellite(500));

		mStage[0].setEngines(1, new Merlin1C());
		mStage[0].setAeroProperties(0.85, 0.3);
		mStage[0].setDryMass(5110);
		mStage[0].setFuelCapacity(29000);
		mStage[0].setPropMass(mStage[0].getFuelCapacity());

		mStage[1].setEngines(1, new Kestrel());
		mStage[1].setAeroProperties(0.85, 0.3);
		mStage[1].setDryMass(665);
		mStage[1].setFuelCapacity(3780);
		mStage[1].setPropMass(mStage[1].getFuelCapacity());

		this.gravTurnTime = 55.0;
	}

}
