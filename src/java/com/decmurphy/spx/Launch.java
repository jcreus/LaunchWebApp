package com.decmurphy.spx;

import static com.decmurphy.spx.physics.Globals.*;
import com.decmurphy.spx.space.Earth;
import com.decmurphy.spx.space.Planet;
import com.decmurphy.spx.vehicle.Falcon9;
import com.decmurphy.spx.vehicle.Payload;
import com.decmurphy.spx.vehicle.DragonV1;

public class Launch {

  public static void main(String[] args) {
    Planet Earth = new Earth(0, 0, 0);
    Payload payload = new DragonV1();
    Falcon9 F9 = new Falcon9(payload);

    boolean SECO = false;
    F9.setClock(-60.0);
    t = 0.0;

    do {

      /*
       *	Operator overloading would make this look at a lot nicer. Pity Java doesn't support it.
       *	Basically what's happening in the 'if' statements here is the equivalent of, for example,
       *	"if(onBoardClock == 167.0) { F9.MECO() }"
       *	Even though 'dt' is an even factor of 1.0, it's not very reliable to say "if(onBoardClock == someTime)".
       *	I'm not sure why.
       */
      if (Math.abs(F9.clock() - profile.getMEITime()) < 0.5 * dt) {
        F9.firstStageIgnition();
      } else if (Math.abs(F9.clock() - profile.getLaunchTime()) < 0.5 * dt) {
        F9.releaseClamps();
      } else if (Math.abs(F9.clock() - profile.getPitchTime()) < 0.5 * dt) {
        F9.pitchKick();
      } else if (Math.abs(F9.clock() - profile.getMECOTime()) < 0.5 * dt) {
        F9.MECO();
      } else if (Math.abs(F9.clock() - profile.getFSSTime()) < 0.5 * dt) {
        F9.stageSeparation();
      } else if (Math.abs(F9.clock() - profile.getSEITime()) < 0.5 * dt) {
        F9.secondStageIgnition();
      }

      if (!SECO && (F9.mStage[1].vel() > 7800 || F9.mStage[1].getPropMass() < 100)) {
        F9.SECO();
        dt = 0.1;
        SECO = true;
      }

      F9.leapfrogStep();
      if (mod(t, 5.0) < dt) {
        F9.outputFile(args[0]);
      }

      t += dt;

    } while (t < 6000);
  }
}
