package com.decmurphy.spx.vehicle;

import com.decmurphy.spx.engine.SuperDraco;

/**
 *
 * @author declan
 */
public class DragonV2 extends SubOrbitalVehicle {

  public DragonV2() {

    mStage[0].setEngines(8, new SuperDraco());
    mStage[0].setAeroProperties(1.83, 0.3);
    mStage[0].setDryMass(10000);
    mStage[0].setFuelCapacity(8*1388);
    mStage[0].setPropMass(mStage[0].getFuelCapacity());

  }
}
