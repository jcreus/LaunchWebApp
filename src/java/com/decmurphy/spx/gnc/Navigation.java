package com.decmurphy.spx.gnc;

import static com.decmurphy.spx.Globals.dt;
import static com.decmurphy.spx.Globals.earthVel;
import static com.decmurphy.spx.Globals.radiusOfEarth;
import static com.decmurphy.spx.Maths.convertCartesianToSpherical;
import static com.decmurphy.spx.Maths.convertSphericalToCartesian;
import static com.decmurphy.spx.Maths.magnitudeOf;
import static com.decmurphy.spx.Maths.rotateY;
import static com.decmurphy.spx.Maths.rotateZ;
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

		double gravityForce = 0.0;
		double thrustForce = 0.0;

		try {
			thrustForce = stage.getThrustAtAltitude(stage.alt());
			gravityForce = stage.getEffectiveMass()*gravityAtRadius(radiusOfEarth + stage.alt());
		} catch (IllegalArgumentException e) {
		}

		stage.force[0] = thrustForce*sin(stage.gamma[0])*cos(stage.gamma[1]) + gravityForce*sin(stage.beta[0])*cos(stage.beta[1]);
		stage.force[1] = thrustForce*sin(stage.gamma[0])*sin(stage.gamma[1]) + gravityForce*sin(stage.beta[0])*sin(stage.beta[1]);
		stage.force[2] = thrustForce*cos(stage.gamma[0])                     + gravityForce*cos(stage.beta[0]);

		stage.accel[0] = stage.force[0]/stage.getEffectiveMass();
		stage.accel[1] = stage.force[1]/stage.getEffectiveMass();
		stage.accel[2] = stage.force[2]/stage.getEffectiveMass();

		stage.absVel[0] = stage.accel[0]*dt/2 + earthVel*sin(stage.beta[0])*sin(stage.beta[1]);
		stage.absVel[1] = stage.accel[1]*dt/2 + earthVel*sin(stage.beta[0])*cos(stage.beta[1]);
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

		double thrustForce = 0.0;
		double dragForce = 0.0;
		double gravityForce = 0.0;

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
				dragForce = stage.getQ()*stage.getAeroProp("Cd")*stage.getAeroProp("XA");
				thrustForce = stage.getPropMass() > 500 ? stage.getThrustAtAltitude(stage.alt()) : 0.0;
				gravityForce = stage.getEffectiveMass()*gravityAtRadius(radiusOfEarth + stage.alt());
			} else {
				dragForce = thrustForce = gravityForce = 0.0;
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

			thrustForce = 0.0;
			gravityForce = 0.0;
			dragForce = 0.0;
		}

		stage.force[0] = thrustForce*sin(stage.gamma[0])*cos(stage.gamma[1]) + dragForce*sin(stage.alpha[0])*cos(stage.alpha[1])	+ gravityForce*sin(stage.beta[0])*cos(stage.beta[1]);
		stage.force[1] = thrustForce*sin(stage.gamma[0])*sin(stage.gamma[1]) + dragForce*sin(stage.alpha[0])*sin(stage.alpha[1])	+ gravityForce*sin(stage.beta[0])*sin(stage.beta[1]);
		stage.force[2] = thrustForce*cos(stage.gamma[0])                     + dragForce*cos(stage.alpha[0])                    	+ gravityForce*cos(stage.beta[0]);

		stage.accel[0] = stage.force[0]/stage.getEffectiveMass();
		stage.accel[1] = stage.force[1]/stage.getEffectiveMass();
		stage.accel[2] = stage.force[2]/stage.getEffectiveMass();
		stage.A = magnitudeOf(stage.accel);

		stage.absVel[0] += stage.accel[0]*dt;
		stage.absVel[1] += stage.accel[1]*dt;
		stage.absVel[2] += stage.accel[2]*dt;
		stage.VA = magnitudeOf(stage.absVel);

		stage.relVel[0] = stage.absVel[0] - earthVel*sin(stage.beta[0])*cos(stage.beta[1]);
		stage.relVel[1] = stage.absVel[1] - earthVel*sin(stage.beta[0])*sin(stage.beta[1]);
		stage.relVel[2] = stage.absVel[2];
		stage.VR = magnitudeOf(stage.relVel);

		stage.alpha[0] = PI - atan2(sqrt(stage.relVel[0]*stage.relVel[0] + stage.relVel[1]*stage.relVel[1]), stage.relVel[2]);
		stage.alpha[1] = PI + atan2(stage.relVel[1], stage.relVel[0]);

		stage.beta[0] = PI - atan2(sqrt(stage.pos[0]*stage.pos[0] + stage.pos[1]*stage.pos[1]), stage.pos[2]);
		stage.beta[1] = PI + atan2(stage.pos[1], stage.pos[0]);

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
	public static void pitchKick(Stage stage, double pitch, double yaw) {

		// A higher value for pitch gives a more extreme pitch-kick
		// A positive yaw aims south, a negative yaw aims north.
		double incl = acos(stage.pos[2]/radiusOfEarth);
		double lon = atan2(stage.pos[1], stage.pos[0]);

    double[] attitude = new double[3];
    convertSphericalToCartesian(new double[]{pitch, PI/2-yaw}, attitude);
    
		rotateY(attitude, incl);
    rotateZ(attitude, lon);
    
    convertCartesianToSpherical(attitude, stage.gamma);
	}

	private static void vectorTransform(Stage stage, double[] arr, double x, double y, Transform dir) {

		double cs, sn;
		double incl = acos(stage.pos[2] / radiusOfEarth);
		int r = (dir == Transform.FORWARD) ? 1 : -1;

		arr[0] = acos(cos(x)*cos(r*incl) - sin(x)*sin(y)*sin(r*incl));
		cs = sin(x)*cos(y) / sin(arr[0]);
		sn = (sin(x)*sin(y)*cos(r*incl) + cos(x)*sin(r*incl)) / sin(arr[0]);
		arr[1] = atan2(sn, cs);

	}

	public static void adjustPitch(Stage stage, double delP) {

		double pitch = stage.gamma[0], yaw = stage.gamma[1];
		double temp1 = stage.beta[0], temp2 = stage.beta[1];

		vectorTransform(stage, stage.gamma, pitch, yaw, Transform.BACKWARD);
		vectorTransform(stage, stage.beta, temp1, temp2, Transform.BACKWARD);

		pitch = (3*PI/2 - stage.beta[0]) - delP;
		yaw = stage.gamma[1];

		temp1 = stage.beta[0];
		temp2 = stage.beta[1];

		vectorTransform(stage, stage.gamma, pitch, yaw, Transform.FORWARD);
		vectorTransform(stage, stage.beta, temp1, temp2, Transform.FORWARD);

	}

	/*
	 *	Flying prograde
	 */
	public static void gravityTurn(Stage stage) {

		if (stage.extraBurnIsUnderway()) {    // Fly retrograde
			stage.gamma[0] = stage.alpha[0];
			stage.gamma[1] = stage.alpha[1];
		} else {                              // Gravity turn
			stage.gamma[0] = PI - stage.alpha[0];
			stage.gamma[1] = PI + stage.alpha[1];
		}
	}

}
