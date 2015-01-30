package com.decmurphy.spx.gnc;

import static com.decmurphy.spx.Globals.dt;
import static com.decmurphy.spx.Globals.earthVel;
import static com.decmurphy.spx.Globals.radiusOfEarth;
import com.decmurphy.spx.vehicle.Stage;
import static com.decmurphy.spx.gnc.Navigation.leapfrogStep;
import static com.decmurphy.spx.gnc.Physics.densityAtAltitude;
import static com.decmurphy.spx.gnc.Physics.gravityAtRadius;
import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

/**
 *
 * @author dmurphy
 */
public class HoverSlam {

  public static double updateLandingThrottle(Stage stage) {

	double minThrottle = 0.7;
	double maxThrottle = 1.0;
	double endHeight;

	if (throttleTest(stage, maxThrottle) > 0.0) {
	  if (throttleTest(stage, minThrottle) < 0.0) {
		do {
		  endHeight = throttleTest(stage, 0.5 * (minThrottle + maxThrottle));
		  if (abs(endHeight) < 1) {
			System.out.println(stage.getParent().clock() + ": New throttle: " + 0.5 * (minThrottle + maxThrottle));
			return 0.5 * (minThrottle + maxThrottle);
		  }
		  minThrottle = endHeight < 0 ? 0.5 * (minThrottle + maxThrottle) : minThrottle;
		  maxThrottle = endHeight > 0 ? 0.5 * (minThrottle + maxThrottle) : maxThrottle;
		} while (true);
	  } else {
		System.out.println(stage.getParent().clock() + ": New throttle: ..0.0");
		return 0.0;
	  }
	} else {
	  System.out.println(stage.getParent().clock() + ": New throttle: ..1.0");
	  return 1.0;
	}
  }

  private static double throttleTest(Stage stage, double throttle) {

	Stage copyStage = new Stage(stage);
	copyStage.setThrottle(throttle);

	double S = copyStage.alt();
	double vel;

	do {

	  copyStage.gamma[0] = copyStage.alpha[0];
	  copyStage.gamma[1] = copyStage.alpha[1];
	  
	  leapfrogStep(copyStage);
	  
	  vel = (copyStage.alt() - S) / dt; // positive or negative
	  S = copyStage.alt();

	} while (vel < 0 && S > 0);

	copyStage = null;
	return S;
  }

}
