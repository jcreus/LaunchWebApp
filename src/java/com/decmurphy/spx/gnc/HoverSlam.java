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
		double dragForce = 0.0;
		double thrustForce = 0.0;
		double gravityForce = 0.0;

		do {

			copyStage.Q = 0.5 * densityAtAltitude(copyStage.alt()) * copyStage.VR * copyStage.VR;

			try {
				dragForce = copyStage.Q * copyStage.Cd * copyStage.XA;
				thrustForce = copyStage.getPropMass() > 500 ? copyStage.getThrustAtAltitude(copyStage.alt()) : 0.0;
				gravityForce = copyStage.getEffectiveMass() * gravityAtRadius(radiusOfEarth + copyStage.alt());
			} catch (IllegalArgumentException e) {
				S = -1.0;
				break;
			}

			copyStage.pos[0] += copyStage.absVel[0] * dt;
			copyStage.pos[1] += copyStage.absVel[1] * dt;
			copyStage.pos[2] += copyStage.absVel[2] * dt;
			copyStage.setPos(sqrt(copyStage.pos[0] * copyStage.pos[0] + copyStage.pos[1] * copyStage.pos[1] + copyStage.pos[2] * copyStage.pos[2]));

			copyStage.force[0] = thrustForce * sin(copyStage.gamma[0]) * sin(copyStage.gamma[1])
							+ dragForce * sin(copyStage.alpha[0]) * sin(copyStage.alpha[1])
							+ gravityForce * sin(copyStage.beta[0]) * sin(copyStage.beta[1]);
			copyStage.force[1] = thrustForce * sin(copyStage.gamma[0]) * cos(copyStage.gamma[1])
							+ dragForce * sin(copyStage.alpha[0]) * cos(copyStage.alpha[1])
							+ gravityForce * sin(copyStage.beta[0]) * cos(copyStage.beta[1]);
			copyStage.force[2] = thrustForce * cos(copyStage.gamma[0])
							+ dragForce * cos(copyStage.alpha[0])
							+ gravityForce * cos(copyStage.beta[0]);

			copyStage.accel[0] = copyStage.force[0] / copyStage.getEffectiveMass();
			copyStage.accel[1] = copyStage.force[1] / copyStage.getEffectiveMass();
			copyStage.accel[2] = copyStage.force[2] / copyStage.getEffectiveMass();

			copyStage.absVel[0] += copyStage.accel[0] * dt;
			copyStage.absVel[1] += copyStage.accel[1] * dt;
			copyStage.absVel[2] += copyStage.accel[2] * dt;

			copyStage.relVel[0] = copyStage.absVel[0] + earthVel * sin(copyStage.beta[0]) * cos(copyStage.beta[1]);
			copyStage.relVel[1] = copyStage.absVel[1] + earthVel * sin(copyStage.beta[0]) * sin(copyStage.beta[1]);
			copyStage.relVel[2] = copyStage.absVel[2];
			copyStage.VR = sqrt(copyStage.relVel[0] * copyStage.relVel[0] + copyStage.relVel[1] * copyStage.relVel[1] + copyStage.relVel[2] * copyStage.relVel[2]);

			copyStage.alpha[0] = PI - atan2(sqrt(copyStage.relVel[0] * copyStage.relVel[0] + copyStage.relVel[1] * copyStage.relVel[1]), copyStage.relVel[2]);
			copyStage.alpha[1] = PI + atan2(copyStage.relVel[0], copyStage.relVel[1]);

			copyStage.gamma[0] = copyStage.alpha[0];
			copyStage.gamma[1] = copyStage.alpha[1];

			copyStage.propMass -= copyStage.throttle * copyStage.numEngines * copyStage.getEngine().getMdot() * dt;

			vel = copyStage.alt() - S; // positive or negative
			S = copyStage.alt();

		} while (vel < 0 && S > 0);

		copyStage = null;
		return S;
	}

}
