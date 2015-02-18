package com.decmurphy.spx.gnc;

import static com.decmurphy.spx.Globals.dt;
import static com.decmurphy.spx.Globals.earthVel;
import static com.decmurphy.spx.Globals.radiusOfEarth;
import static com.decmurphy.spx.Maths.magnitudeOf;
import static com.decmurphy.spx.Physics.densityAtAltitude;
import static com.decmurphy.spx.Physics.gravityAtRadius;
import com.decmurphy.spx.vehicle.Stage;
import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.max;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 *
 * @author dmurphy
 */
public class HoverSlam {

	public static double updateLandingThrottle(Stage stage) {

		DecimalFormat df = new DecimalFormat();
    df.setRoundingMode(RoundingMode.HALF_UP);
		
		double minThrottle = 0.7;
		double maxThrottle = 1.0;
		double currentThrottle = stage.getThrottle();

		if (throttleTest(stage, currentThrottle) > 0.0) {
			maxThrottle = currentThrottle;
		} else {
			minThrottle = max(minThrottle, currentThrottle);
		}

		if (throttleTest(stage, maxThrottle) > 0.0) {
			if (throttleTest(stage, minThrottle) < 0.0) {
				do {
					double testThrottle = 0.5 * (minThrottle + maxThrottle);
					double endHeight = throttleTest(stage, testThrottle);
					if (abs(endHeight) < 1) {
						System.out.println(df.format(stage.getParent().clock()) + ": New throttle: " + testThrottle + " --S: " + stage.alt() + " --V: " + stage.relVel());
						return testThrottle;
					}
					minThrottle = endHeight < 0 ? testThrottle : minThrottle;
					maxThrottle = endHeight > 0 ? testThrottle : maxThrottle;
				} while (true);
			} else {
				System.out.println(df.format(stage.getParent().clock()) + ": New throttle: ..0.0" + " --S: " + stage.alt() + " --V: " + stage.relVel());
				return 0.0;
			}
		} else {
			System.out.println(df.format(stage.getParent().clock()) + ": New throttle: ..1.0" + " --S: " + stage.alt() + " --V: " + stage.relVel());
			return 1.0;
		}
	}

	private static double throttleTest(Stage stage, double throttle) {

		Stage copyStage = new Stage(stage);
		copyStage.setThrottle(throttle);

		double S = copyStage.alt();
		double vel;

		do {

			copyStage.setQ(0.5*densityAtAltitude(copyStage.alt())*copyStage.VR*copyStage.VR);
			copyStage.alpha[0] = copyStage.getQ() * copyStage.getAeroProp("Cd") * copyStage.getAeroProp("XA");
			copyStage.beta[0]  = copyStage.getEffectiveMass() * gravityAtRadius(radiusOfEarth + copyStage.alt());
			copyStage.gamma[0] = copyStage.getPropMass() > 500 ? copyStage.getThrustAtAltitude(copyStage.alt()) : 0.0;

			copyStage.force[0] = copyStage.gamma[0]*sin(copyStage.gamma[1])*cos(copyStage.gamma[2]) + copyStage.alpha[0]*sin(copyStage.alpha[1])*cos(copyStage.alpha[2]) + copyStage.beta[0]*sin(copyStage.beta[1])*cos(copyStage.beta[2]);
			copyStage.force[1] = copyStage.gamma[0]*sin(copyStage.gamma[1])*sin(copyStage.gamma[2]) + copyStage.alpha[0]*sin(copyStage.alpha[1])*sin(copyStage.alpha[2]) + copyStage.beta[0]*sin(copyStage.beta[1])*sin(copyStage.beta[2]);
			copyStage.force[2] = copyStage.gamma[0]*cos(copyStage.gamma[1])                         + copyStage.alpha[0]*cos(copyStage.alpha[1])                         + copyStage.beta[0]*cos(copyStage.beta[1]);

			copyStage.pos[0] += copyStage.absVel[0]*dt;
			copyStage.pos[1] += copyStage.absVel[1]*dt;
			copyStage.pos[2] += copyStage.absVel[2]*dt;
			copyStage.S = magnitudeOf(copyStage.pos);

			copyStage.accel[0] = copyStage.force[0]/copyStage.getEffectiveMass();
			copyStage.accel[1] = copyStage.force[1]/copyStage.getEffectiveMass();
			copyStage.accel[2] = copyStage.force[2]/copyStage.getEffectiveMass();
			copyStage.A = magnitudeOf(copyStage.accel);

			copyStage.absVel[0] += copyStage.accel[0]*dt;
			copyStage.absVel[1] += copyStage.accel[1]*dt;
			copyStage.absVel[2] += copyStage.accel[2]*dt;
			copyStage.VA = magnitudeOf(copyStage.absVel);

			copyStage.relVel[0] = copyStage.absVel[0] - earthVel*sin(copyStage.beta[0])*sin(copyStage.beta[1]);
			copyStage.relVel[1] = copyStage.absVel[1] + earthVel*sin(copyStage.beta[0])*cos(copyStage.beta[1]);
			copyStage.relVel[2] = copyStage.absVel[2];
			copyStage.VR = magnitudeOf(copyStage.relVel);

			copyStage.alpha[1] = PI - atan2(sqrt(copyStage.relVel[0]*copyStage.relVel[0] + copyStage.relVel[1]*copyStage.relVel[1]), copyStage.relVel[2]);
			copyStage.alpha[2] = PI + atan2(copyStage.relVel[0], copyStage.relVel[1]);

			copyStage.beta[1] = PI - atan2(sqrt(copyStage.pos[0]*copyStage.pos[0] + copyStage.pos[1]*copyStage.pos[1]), copyStage.pos[2]);
			copyStage.beta[2] = PI + atan2(copyStage.pos[0], copyStage.pos[1]);

			copyStage.gamma[1] = copyStage.alpha[1];
			copyStage.gamma[2] = copyStage.alpha[2];

			copyStage.setPropMass(copyStage.getPropMass() - copyStage.getThrottle() * copyStage.getNumEngines() * copyStage.getEngine().getMdot() * dt);

			vel = (copyStage.alt() - S)/dt; // radial velocity
			S = copyStage.alt();

		} while (vel <= 0.0 && S >= 0.0 && copyStage.getPropMass() > 500.0);

		copyStage = null;
		return S;
	}

}
