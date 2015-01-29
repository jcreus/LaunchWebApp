package com.decmurphy.spx.gnc;

import com.decmurphy.spx.vehicle.Stage;
import static com.decmurphy.spx.Globals.dt;
import static com.decmurphy.spx.Globals.radiusOfEarth;
import static com.decmurphy.spx.gnc.Navigation.gravityTurn;
import static com.decmurphy.spx.gnc.Physics.densityAtAltitude;
import static com.decmurphy.spx.gnc.Physics.gravityAtAltitude;
import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

/**
 *
 * @author dmurphy
 */
public class HoverSlam {

  public static double updateLandingThrottle(Stage stage) {
	Stage copyStage = new Stage(stage);
	return getLandingThrottle(copyStage, copyStage.alt(), copyStage.vectorVel(), copyStage.getPropMass());
  }

  private static double getLandingThrottle(Stage stage, double alt, double[] vectorVel, double propMass) {

	double minThrottle = 0.7;
	double maxThrottle = 1.0;
	double endHeight;

	if (throttleTest(stage, alt, vectorVel, propMass, maxThrottle) > 0.0) {
	  if (throttleTest(stage, alt, vectorVel, propMass, minThrottle) < 0.0) {
		do {
		  endHeight = throttleTest(stage, alt, vectorVel, propMass, 0.5 * (minThrottle + maxThrottle));
		  if (abs(endHeight) < 0.1) {
			return 0.5 * (minThrottle + maxThrottle);
		  }
		  minThrottle = endHeight < 0 ? 0.5 * (minThrottle + maxThrottle) : minThrottle;
		  maxThrottle = endHeight > 0 ? 0.5 * (minThrottle + maxThrottle) : maxThrottle;
		} while (true);
	  } else {
		return 0.0;
	  }
	} else {
	  return 1.0;
	}
  }

  private static double throttleTest(Stage stage, double alt, double[] vel, double propMass, double throttle) {

	double VEL, ft, fd, mass;
	double fx, fy, ax, ay;

	VEL = sqrt(vel[0] * vel[0] + vel[1] * vel[1] + vel[2] * vel[2]);

	ft = throttle * stage.getThrustAtAltitude(alt);

	do {
	  propMass -= throttle * 236 * dt;
	  mass = propMass + stage.getDryMass();

	  fd = (0.5) * stage.Cd * stage.A * densityAtAltitude(stage.alt()) * VEL * VEL;

	  gravityTurn(stage);

	  fx = ft * cos(stage.gamma[0]) + fd * cos(stage.alpha[0] + PI) + mass * gravityAtAltitude(alt) * cos(stage.beta[0] + PI);
	  ax = fx / mass;
	  vel[0] += ax * dt;

	  fy = ft * sin(stage.gamma[0]) + fd * sin(stage.alpha[0] + PI) + mass * gravityAtAltitude(alt) * sin(stage.beta[0] + PI);
	  alt += vel[1] * dt;
	  ay = fy / mass;
	  vel[1] += ay * dt;

	  VEL = sqrt(vel[0] * vel[0] + vel[1] * vel[1] + vel[2] * vel[2]);
	  
	} while (abs(VEL) < 1);

	return alt - radiusOfEarth;
  }

}
