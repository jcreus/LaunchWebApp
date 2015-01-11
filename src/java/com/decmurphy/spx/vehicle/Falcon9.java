package com.decmurphy.spx.vehicle;

import static com.decmurphy.spx.Globals.incl;
import static com.decmurphy.spx.Globals.lon;
import com.decmurphy.spx.engine.Merlin1C;
import com.decmurphy.spx.engine.Merlin1Cv;
import com.decmurphy.spx.payload.DragonV1;
import com.decmurphy.spx.payload.Payload;

/**
 *
 * @author dmurphy
 */
public class Falcon9 extends TwoStageRocket {

	public Falcon9(Payload payload) {
		this();
		setPayload(payload);
	}
	
	public Falcon9()	{
		
		Merlin1C M1C = new Merlin1C();
		Merlin1Cv M1Cv = new Merlin1Cv();

		setPayload(new DragonV1());

		mStage[0].setEngines(9, M1C);
		mStage[0].setAeroProperties(1.83, 0.3);
		mStage[0].setDryMass(15000);
		mStage[0].setFuelCapacity(30000);
		mStage[0].setPropMass(mStage[0].getFuelCapacity());
		mStage[0].setThrottle(0.0);

		mStage[1].setEngines(1, M1Cv);
		mStage[1].setAeroProperties(1.83, 0.3);
		mStage[1].setDryMass(4000);
		mStage[1].setFuelCapacity(65000);
		mStage[1].setPropMass(mStage[1].getFuelCapacity());
		mStage[1].setLegs(false);
		mStage[1].setThrottle(0.0);

		this.setCoordinates(incl, lon);
		this.setClock(-60.0);
		this.gravTurnTime = 55.0;
	}
	
	private void setPayload(Payload p) {
		this.payload = p;
	}
	
}