package com.decmurphy.spx.gnc;

import static com.decmurphy.spx.gnc.Navigation.gravityTurn;
import static com.decmurphy.spx.gnc.Navigation.leapfrogStep;
import com.decmurphy.spx.vehicle.Stage;
import static com.decmurphy.utils.Globals.dt;
import com.decmurphy.utils.Maths;
import static com.decmurphy.utils.Maths.magnitudeOf;
import static java.lang.Math.abs;
import static java.lang.Math.max;
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

		if (throttleTest(stage, maxThrottle) > 0.0) {        // If maxThrottle stops you above ground
			if (throttleTest(stage, minThrottle) < 0.0) {      // If minThrottle makes you crash
				do {
					
					double testThrottle = 0.5*(minThrottle + maxThrottle);		// Test average of two			
					double endHeight = throttleTest(stage, testThrottle);     // Find end altitude
					
					if (abs(endHeight) < 1) {
						System.out.println(df.format(stage.getParent().clock()) + ": New throttle: " + testThrottle + " --S: " + stage.alt() + " --V: " + stage.relVel());
						return testThrottle;
					}
					
					minThrottle = endHeight < 0 ? testThrottle : minThrottle;
					maxThrottle = endHeight > 0 ? testThrottle : maxThrottle;
					
					// Stalls sometimes when fuel runs out. This breaks out of the stall
					if(abs(minThrottle - maxThrottle) < 1e-3)
						return minThrottle;
					
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
		double[] vel;

		do {

			leapfrogStep(copyStage);
			gravityTurn(copyStage);

			vel = new Maths.CartesianVelocity(copyStage.relVel)
					.convertToSpherical(new Maths.CartesianCoordinates(copyStage.relPos))
					.getValues();
			
			S = copyStage.alt();
			
			if(S <= 0) break;                          // If crash, S<0, started too late
			if(copyStage.getPropMass() < 500.0) break; // If fuel runs out, S>0, started too soon
			if(magnitudeOf(vel) < 6.0)	break;         // If stopped, S>0, started too soon

		} while (true);

		copyStage = null;
		return S;

	}

}
