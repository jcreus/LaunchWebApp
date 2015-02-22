package com.decmurphy.spx.gnc;

import static com.decmurphy.spx.gnc.Navigation.gravityTurn;
import static com.decmurphy.spx.gnc.Navigation.leapfrogStep;
import com.decmurphy.spx.vehicle.Stage;
import static com.decmurphy.utils.Globals.dt;
import static com.decmurphy.utils.Globals.earthVel;
import static com.decmurphy.utils.Globals.radiusOfEarth;
import static com.decmurphy.utils.Maths.magnitudeOf;
import static com.decmurphy.utils.Physics.densityAtAltitude;
import static com.decmurphy.utils.Physics.gravityAtRadius;
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
		copyStage.setThrottleTest(true);

		double S = copyStage.alt();
		double vel;

		do {

			leapfrogStep(copyStage);
			gravityTurn(copyStage);

			vel = (copyStage.alt() - S)/dt; // radial velocity
			S = copyStage.alt();

		} while (vel <= 0.0 && S >= 0.0 && copyStage.getPropMass() > 500.0);

		copyStage = null;
		return S;
	}

}
