package com.decmurphy.spx.vehicle;

import static com.decmurphy.spx.physics.Globals.*;
import com.decmurphy.spx.engine.Merlin1D;
import com.decmurphy.spx.engine.Merlin1Dv;

public class Falcon9 extends TwoStageRocket
{

	public boolean mLegsExtended;

	public Falcon9(Payload payload)
	{
		Merlin1D M1D = new Merlin1D();
		Merlin1Dv M1Dv = new Merlin1Dv();

		this.payload = new Payload(payload);

		mStage[0].setEngines(9, M1D);
		mStage[0].setAeroProperties(1.83, 0.3);
		mStage[0].setDryMass(18000);
		mStage[0].setFuelCapacity(390000);
		mStage[0].setPropMass(mStage[0].getFuelCapacity());
		mStage[0].setLegs(false);
		mStage[0].setThrottle(0.0);

		mStage[1].setEngines(1, M1Dv);
		mStage[1].setAeroProperties(1.83, 0.3);
		mStage[1].setDryMass(4900);
		mStage[1].setFuelCapacity(75700);
		mStage[1].setPropMass(mStage[1].getFuelCapacity());
		mStage[1].setLegs(false);
		mStage[1].setThrottle(0.0);

		mLegsExtended = false;

		this.setCoordinates(incl, lon);
		this.setClock(-60.0);
		this.gravTurnTime = 55.0;
	}

	public void extendLegs()
	{
		mLegsExtended = true;
	}

}
