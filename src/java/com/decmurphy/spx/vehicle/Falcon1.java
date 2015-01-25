package com.decmurphy.spx.vehicle;

import com.decmurphy.spx.engine.Kestrel;
import com.decmurphy.spx.engine.Merlin1C;
import com.decmurphy.spx.mission.Mission;
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

		Merlin1C M1C = new Merlin1C();
		Kestrel Kes = new Kestrel();
		
		setPayload(new Satellite(500));

		mStage[0].setEngines(1, M1C);
		mStage[0].setAeroProperties(0.85, 0.3);
		mStage[0].setDryMass(5110);
		mStage[0].setFuelCapacity(29000);
		mStage[0].setPropMass(mStage[0].getFuelCapacity());
		mStage[0].setThrottle(0.0);

		mStage[1].setEngines(1, Kes);
		mStage[1].setAeroProperties(0.85, 0.3);
		mStage[1].setDryMass(665);
		mStage[1].setFuelCapacity(3780);
		mStage[1].setPropMass(mStage[1].getFuelCapacity());
		mStage[1].setLegs(false);
		mStage[1].setThrottle(0.0);

		this.gravTurnTime = 55.0;
	}

}
