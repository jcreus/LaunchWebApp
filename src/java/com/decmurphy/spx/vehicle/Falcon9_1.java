package com.decmurphy.spx.vehicle;

import static com.decmurphy.spx.Globals.incl;
import static com.decmurphy.spx.Globals.lon;
import com.decmurphy.spx.engine.Merlin1D;
import com.decmurphy.spx.engine.Merlin1Dv;
import com.decmurphy.spx.payload.DragonV1;
import com.decmurphy.spx.payload.Payload;

public class Falcon9_1 extends TwoStageRocket {

	public boolean mLegsExtended;

	public Falcon9_1(Payload payload) {
		this();
		setPayload(payload);
	}
	
	public Falcon9_1() {

		Merlin1D M1D = new Merlin1D();
		Merlin1Dv M1Dv = new Merlin1Dv();
		
		setPayload(new DragonV1());

		mStage[0].setEngines(9, M1D);
		mStage[0].setAeroProperties(1.83, 0.3);
		mStage[0].setDryMass(mStage[0].hasLegs() ? 20000 : 18000);
		mStage[0].setFuelCapacity(390000);
		mStage[0].setPropMass(mStage[0].getFuelCapacity());
		mStage[0].setThrottle(0.0);

		mStage[1].setEngines(1, M1Dv);
		mStage[1].setAeroProperties(1.83, 0.3);
		mStage[1].setDryMass(4900);
		mStage[1].setFuelCapacity(75700);
		mStage[1].setPropMass(mStage[1].getFuelCapacity());
		mStage[1].setThrottle(0.0);

		mLegsExtended = false;

		this.setCoordinates(incl, lon);
		this.gravTurnTime = 55.0;
	}

	public void extendLegs() {
		mLegsExtended = true;
	}

}
