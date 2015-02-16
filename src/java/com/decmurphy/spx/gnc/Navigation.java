package com.decmurphy.spx.gnc;

import static com.decmurphy.spx.Globals.dt;
import static com.decmurphy.spx.Globals.earthVel;
import static com.decmurphy.spx.Globals.radiusOfEarth;
import com.decmurphy.spx.Maths.CartesianCoordinates;
import com.decmurphy.spx.Maths.SphericalCoordinates;
import static com.decmurphy.spx.Maths.magnitudeOf;
import static com.decmurphy.spx.Physics.densityAtAltitude;
import static com.decmurphy.spx.Physics.gravityAtRadius;
import com.decmurphy.spx.vehicle.Stage;
import static java.lang.Math.PI;
import static java.lang.Math.acos;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

/**
 *
 * @author declan
 */
public class Navigation {

	private enum Transform {

		FORWARD,
		BACKWARD
	}

	public static void leapfrogFirstStep(Stage stage) {

		stage.beta[0] = 0.0;
		stage.gamma[0] = 0.0;

		try {
			stage.beta[0] = stage.getEffectiveMass()*gravityAtRadius(radiusOfEarth + stage.alt());
			stage.gamma[0] = stage.getThrustAtAltitude(stage.alt());
		} catch (IllegalArgumentException e) {
		}

		stage.force[0] = stage.gamma[0]*sin(stage.gamma[1])*cos(stage.gamma[2]) + stage.beta[0]*sin(stage.beta[1])*cos(stage.beta[2]);
		stage.force[1] = stage.gamma[0]*sin(stage.gamma[1])*sin(stage.gamma[2]) + stage.beta[0]*sin(stage.beta[1])*sin(stage.beta[2]);
		stage.force[2] = stage.gamma[0]*cos(stage.gamma[1])                     + stage.beta[0]*cos(stage.beta[1]);

		stage.accel[0] = stage.force[0]/stage.getEffectiveMass();
		stage.accel[1] = stage.force[1]/stage.getEffectiveMass();
		stage.accel[2] = stage.force[2]/stage.getEffectiveMass();

		stage.absVel[0] = stage.accel[0]*dt/2 + earthVel*sin(stage.beta[1])*sin(stage.beta[2]);
		stage.absVel[1] = stage.accel[1]*dt/2 + earthVel*sin(stage.beta[1])*cos(stage.beta[2]);
		stage.absVel[2] = stage.accel[2]*dt/2;

		stage.relVel[0] = 0;
		stage.relVel[1] = 0;
		stage.relVel[2] = 0;

		stage.S = magnitudeOf(stage.pos);
		stage.A = magnitudeOf(stage.accel);

		stage.isMoving = true;

	}

	/*
	 *	This function is where the magic happens. Evaluate forces on vehicle, update
   *  accelerations, velocities, positions.	Update angles. Execute gravity turn
   *  starting at T+0:55. And don't forget to remove the appropriate amount of mass
   *  from the system.
	 */
	public static void leapfrogStep(Stage stage) {

		stage.gamma[0] = 0.0;
		stage.alpha[0] = 0.0;
		stage.beta[0] = 0.0;

		stage.pos[0] += stage.absVel[0]*dt;
		stage.pos[1] += stage.absVel[1]*dt;
		stage.pos[2] += stage.absVel[2]*dt;
		stage.S = magnitudeOf(stage.pos);

		stage.relPos[0] += stage.relVel[0]*dt;
		stage.relPos[1] += stage.relVel[1]*dt;
		stage.relPos[2] += stage.relVel[2]*dt;
		stage.relS = magnitudeOf(stage.relPos);

		try {
			stage.setQ(0.5*densityAtAltitude(stage.alt())*stage.VR*stage.VR);
			if (stage.getPropMass() < 500) stage.setThrottle(0.0);

			if (stage.isMoving) {
				stage.alpha[0] = stage.getQ()*stage.getAeroProp("Cd")*stage.getAeroProp("XA");
				stage.gamma[0] = stage.getPropMass() > 500 ? stage.getThrustAtAltitude(stage.alt()) : 0.0;
				stage.beta[0] = stage.getEffectiveMass()*gravityAtRadius(radiusOfEarth + stage.alt());
			} else {
				stage.alpha[0] = stage.gamma[0] = stage.beta[0] = 0.0;
			}
		} catch (IllegalArgumentException e) {
		}

		/*
		 *	If a crash happens (i.e if distance from earth's centre < earth's radius) then turn off engines.
		 *	Introduce an upwards reaction force (Newton's 3rd law) which effectively cancels out gravity.
		 *	There should be no more movement after this point.
		 *
		 *	I had to introduce the 'if(stage.clock())' statement because for a split second at the very start,
		 *	the stage falls before it rises. I'll find a more elegant solution to this problem soon.
		 */
		if (stage.alt() < 0) {
			if (stage.landingBurnIsUnderway()) {
				System.out.printf("T%+7.2f\t%.32s\n", stage.clock(), "Crash/Landing");
			}

      if(stage.clock() > 5.0)
        stage.setLanded(true);
      
			for (int i = 0; i < 3; i++) {
				stage.pos[i] -= stage.absVel[i]*dt;
				stage.relPos[i] -= stage.relVel[i]*dt;
				stage.absVel[i] = 0.0;
			}
  		stage.relS = magnitudeOf(stage.relPos);
			stage.S = magnitudeOf(stage.pos);
			if (stage.clock() > 100.0) {
				stage.setThrottle(0.0);
			}
			stage.setLandingBurnIsUnderway(false);
			stage.isMoving = false;

			stage.gamma[0] = 0.0;
			stage.beta[0] = 0.0;
			stage.alpha[0] = 0.0;
		}

		stage.force[0] = stage.gamma[0]*sin(stage.gamma[1])*cos(stage.gamma[2]) + stage.alpha[0]*sin(stage.alpha[1])*cos(stage.alpha[2])	+ stage.beta[0]*sin(stage.beta[1])*cos(stage.beta[2]);
		stage.force[1] = stage.gamma[0]*sin(stage.gamma[1])*sin(stage.gamma[2]) + stage.alpha[0]*sin(stage.alpha[1])*sin(stage.alpha[2])	+ stage.beta[0]*sin(stage.beta[1])*sin(stage.beta[2]);
		stage.force[2] = stage.gamma[0]*cos(stage.gamma[1])                     + stage.alpha[0]*cos(stage.alpha[1])                    	+ stage.beta[0]*cos(stage.beta[1]);

		stage.accel[0] = stage.force[0]/stage.getEffectiveMass();
		stage.accel[1] = stage.force[1]/stage.getEffectiveMass();
		stage.accel[2] = stage.force[2]/stage.getEffectiveMass();
		stage.A = magnitudeOf(stage.accel);

		stage.absVel[0] += stage.accel[0]*dt;
		stage.absVel[1] += stage.accel[1]*dt;
		stage.absVel[2] += stage.accel[2]*dt;
		stage.VA = magnitudeOf(stage.absVel);

		stage.relVel[0] = stage.absVel[0] - earthVel*sin(stage.beta[1])*cos(stage.beta[2]);
		stage.relVel[1] = stage.absVel[1] - earthVel*sin(stage.beta[1])*sin(stage.beta[2]);
		stage.relVel[2] = stage.absVel[2];
		stage.VR = magnitudeOf(stage.relVel);

		stage.alpha[1] = PI - atan2(sqrt(stage.relVel[0]*stage.relVel[0] + stage.relVel[1]*stage.relVel[1]), stage.relVel[2]);
		stage.alpha[2] = PI + atan2(stage.relVel[1], stage.relVel[0]);

		stage.beta[1] = PI - atan2(sqrt(stage.pos[0]*stage.pos[0] + stage.pos[1]*stage.pos[1]), stage.pos[2]);
		stage.beta[2] = PI + atan2(stage.pos[1], stage.pos[0]);

		stage.setPropMass(stage.getPropMass() - stage.getThrottle()*stage.getNumEngines()*stage.getEngine().getMdot()*dt);

	}

	/*
	 *	3D is hard. Honestly it's like an order of magnitude harder than 2D.
	 *	My current method for any kind of operation is to transform it into an 
   *  easier-to-work-with coordinate system, do the operation, and then transform
   *  back. For the pitch kick I can skip the first step. So rotate (pitch, yaw)
   *  by theta degrees about the y-axis and then rotate by phi degrees about the z-axis.
	 *
	 *	For Cape Canaveral, this is rotating (pitch, yaw) by -61.51 degrees about y,
   *  and then by -80.58 degrees about z.	Voila. That's your heading after pitch-kick.
   *  Hint: Use the right-hand rule!
	 */
	public static void pitchKick(Stage s, double pitch, double yaw) {

		// A higher value for pitch gives a more extreme pitch-kick
		// A positive yaw aims south, a negative yaw aims north.
		double incl = acos(s.pos[2]/radiusOfEarth);
		double lon = atan2(s.pos[1], s.pos[0]);

    SphericalCoordinates attitudeS = new SphericalCoordinates(radiusOfEarth, pitch, yaw);
    CartesianCoordinates attitude = attitudeS.convertToCartesian();
    
    // Always start off pointing in the positive y-direction.
    attitude.rotateZ(PI/2);
    // Then rotate down the prime meridian to the correct inclination ...
		attitude.rotateY(incl);
    // ... and around to the correct longitude
    attitude.rotateZ(lon);
    
    // Loop not zero based since we don't care about the magnitude of the
    // thrust here, only the direction.
    for(int i=1;i<s.gamma.length;i++)
      s.gamma[i] = attitude.convertToSpherical().get(i);
	}

	private static void vectorTransform(Stage s, double[] arr, Transform dir) {

    double incl = acos(s.pos[2]/radiusOfEarth);
    double lon = atan2(s.pos[1], s.pos[0]);
    
    // arr is in Spherical form already, but we create an explicit object here
    // to make rotations and conversions easier. 
    SphericalCoordinates sph = new SphericalCoordinates(arr[0], arr[1], arr[2]);
    CartesianCoordinates newVector = sph.convertToCartesian();
    
    int r;
    switch (dir) {
      case BACKWARD: r = -1; newVector.rotateZ(r*incl); newVector.rotateY(r*lon); break;
      case FORWARD: r = 1; newVector.rotateY(r*lon); newVector.rotateZ(r*incl); break;
      default: throw new IllegalArgumentException("Invalid Transform direction");
    }

    // Not zero based for the same reasons as the pitch kick
    for(int i=1;i<arr.length;i++)
      arr[i] = newVector.convertToSpherical().get(i);
 
	}

	public static void adjustPitch(Stage stage, double delP) {

    if(stage.gamma[0]>0.0) {
      vectorTransform(stage, stage.gamma, Transform.BACKWARD);
      vectorTransform(stage, stage.beta, Transform.BACKWARD);
    
		  stage.gamma[1] = (3*PI/2 - stage.beta[1]) - delP;

      vectorTransform(stage, stage.gamma, Transform.FORWARD);
      vectorTransform(stage, stage.beta, Transform.FORWARD);
    }
	}

	/*
	 *	Flying prograde
	 */
	public static void gravityTurn(Stage stage) {

		if (stage.extraBurnIsUnderway()) {    // Fly retrograde
			stage.gamma[1] = stage.alpha[1];
			stage.gamma[2] = stage.alpha[2];
		} else {                              // Gravity turn
			stage.gamma[1] = PI - stage.alpha[1];
			stage.gamma[2] = PI + stage.alpha[2];
		}
	}

}
