package com.decmurphy.spx.gnc;

import static com.decmurphy.spx.Globals.dt;
import static com.decmurphy.spx.Globals.earthVel;
import static com.decmurphy.spx.Globals.gravConstant;
import static com.decmurphy.spx.Globals.incl;
import static com.decmurphy.spx.Globals.lon;
import static com.decmurphy.spx.Globals.massOfEarth;
import static com.decmurphy.spx.Globals.radiusOfEarth;
import com.decmurphy.spx.vehicle.Stage;
import static java.lang.Math.PI;
import static java.lang.Math.acos;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

/**
 *
 * @author declan
 */
public class Navigation {

	public static void leapfrogFirstStep(Stage stage) {
		
		double gravityForce = 0.0;
		double thrustForce = 0.0;
		
		try {
			thrustForce = stage.getThrustAtAltitude(stage.alt());
			gravityForce = stage.getMass() * gravityAtRadius(radiusOfEarth + stage.alt());
		} catch (IllegalArgumentException e) {
		}

		stage.force[0] = thrustForce * sin(stage.gamma[0]) * sin(stage.gamma[1]) + gravityForce * sin(stage.beta[0]) * sin(stage.beta[1]);
		stage.force[1] = thrustForce * sin(stage.gamma[0]) * cos(stage.gamma[1]) + gravityForce * sin(stage.beta[0]) * cos(stage.beta[1]);
		stage.force[2] = thrustForce * cos(stage.gamma[0]) + gravityForce * cos(stage.beta[0]);

		stage.accel[0] = stage.force[0] / stage.getMass();
		stage.accel[1] = stage.force[1] / stage.getMass();
		stage.accel[2] = stage.force[2] / stage.getMass();

		stage.absVel[0] = stage.accel[0] * dt / 2 - earthVel * sin(stage.beta[0]) * cos(stage.beta[1]);
		stage.absVel[1] = stage.accel[1] * dt / 2 - earthVel * sin(stage.beta[0]) * sin(stage.beta[1]);
		stage.absVel[2] = stage.accel[2] * dt / 2;

		stage.relVel[0] = 0;
		stage.relVel[1] = 0;
		stage.relVel[2] = 0;

		stage.setPos(sqrt(stage.pos[0] * stage.pos[0] + stage.pos[1] * stage.pos[1] + stage.pos[2] * stage.pos[2]));
		stage.setAccel(sqrt(stage.accel[0] * stage.accel[0] + stage.accel[1] * stage.accel[1] + stage.accel[2] * stage.accel[2]));

		stage.isMoving = true;
	}

	/*
	 *	This function is where the magic happens. Evaluate forces on vehicle, update accelerations, velocities, positions.
	 *	Update angles.
	 *	Execute gravity turn starting at T+0:55
	 *	And don't forget to remove the appropriate amount of mass from the system.
	 */
	public static void leapfrogStep(Stage stage) {
		
		double thrustForce = 0.0;
		double dragForce = 0.0;
		double gravityForce = 0.0;

		try {
			stage.Q = 0.5 * densityAtAltitude(stage.alt()) * stage.VR * stage.VR;

			if (stage.isMoving) {
				dragForce = stage.Q * stage.Cd * stage.XA;
				thrustForce = stage.getThrustAtAltitude(stage.alt());
				gravityForce = stage.getMass() * gravityAtRadius(radiusOfEarth + stage.alt());
			} else {
				dragForce = thrustForce = gravityForce = 0.0;
			}
		} catch (IllegalArgumentException e) {
			if (stage.VR < 5) {
				System.out.printf(stage.name + " Touchdown wooooooo\n");
			} else {
				System.out.printf("Looks like your " + stage.name + " crashed m8.\n");
			}

		}

		stage.pos[0] += stage.absVel[0] * dt;
		stage.pos[1] += stage.absVel[1] * dt;
		stage.pos[2] += stage.absVel[2] * dt;

		stage.filepos[0] += stage.relVel[0] * dt;
		stage.filepos[1] += stage.relVel[1] * dt;
		stage.filepos[2] += stage.relVel[2] * dt;

		stage.setPos(sqrt(stage.pos[0] * stage.pos[0] + stage.pos[1] * stage.pos[1] + stage.pos[2] * stage.pos[2]));

		/*
		 *	If a crash happens (i.e if distance from earth's centre < earth's radius) then turn off engines.
		 *	Introduce an upwards reaction force (Newton's 3rd law) which effectively cancels out gravity.
		 *	There should be no more movement after this point.
		 *
		 *	I had to introduce the 'if(onBoardClock)' statement because for a split second at the very start,
		 *	the stage falls before it rises. I'll find a more elegant solution to this problem soon.
		 */
		if (stage.alt() < 0) {
			int i;
			for (i = 0; i < 3; i++) {
				stage.pos[i] -= stage.absVel[i] * dt;
				stage.filepos[i] -= stage.relVel[i] * dt;
				stage.absVel[i] = 0.0;
			}
			stage.setPos(sqrt(stage.pos[0] * stage.pos[0] + stage.pos[1] * stage.pos[1] + stage.pos[2] * stage.pos[2]));

//			if(F9.onBoardClock > 1.0) setThrottle(0.0);
			gravityForce = 0.0;
		}

		stage.force[0] = thrustForce * sin(stage.gamma[0]) * sin(stage.gamma[1])
						+ dragForce * sin(stage.alpha[0]) * sin(stage.alpha[1])
						+ gravityForce * sin(stage.beta[0]) * sin(stage.beta[1]);
		stage.force[1] = thrustForce * sin(stage.gamma[0]) * cos(stage.gamma[1])
						+ dragForce * sin(stage.alpha[0]) * cos(stage.alpha[1])
						+ gravityForce * sin(stage.beta[0]) * cos(stage.beta[1]);
		stage.force[2] = thrustForce * cos(stage.gamma[0])
						+ dragForce * cos(stage.alpha[0])
						+ gravityForce * cos(stage.beta[0]);

		stage.accel[0] = stage.force[0] / stage.getMass();
		stage.accel[1] = stage.force[1] / stage.getMass();
		stage.accel[2] = stage.force[2] / stage.getMass();
		stage.setAccel(sqrt(stage.accel[0] * stage.accel[0] + stage.accel[1] * stage.accel[1] + stage.accel[2] * stage.accel[2]));

		stage.absVel[0] += stage.accel[0] * dt;
		stage.absVel[1] += stage.accel[1] * dt;
		stage.absVel[2] += stage.accel[2] * dt;
		stage.VA = sqrt(stage.absVel[0] * stage.absVel[0] + stage.absVel[1] * stage.absVel[1] + stage.absVel[2] * stage.absVel[2]);

		stage.relVel[0] = stage.absVel[0] + earthVel * sin(stage.beta[0]) * cos(stage.beta[1]);
		stage.relVel[1] = stage.absVel[1] + earthVel * sin(stage.beta[0]) * sin(stage.beta[1]);
		stage.relVel[2] = stage.absVel[2];
		stage.VR = sqrt(stage.relVel[0] * stage.relVel[0] + stage.relVel[1] * stage.relVel[1] + stage.relVel[2] * stage.relVel[2]);

		stage.alpha[0] = PI - atan2(sqrt(stage.relVel[0] * stage.relVel[0] + stage.relVel[1] * stage.relVel[1]), stage.relVel[2]);
		stage.alpha[1] = PI + atan2(stage.relVel[0], stage.relVel[1]);

		stage.beta[0] = PI - atan2(sqrt(stage.pos[0] * stage.pos[0] + stage.pos[1] * stage.pos[1]), stage.pos[2]);
		stage.beta[1] = PI + atan2(stage.pos[0], stage.pos[1]);

		stage.propMass -= stage.throttle * stage.numEngines * stage.getEngine().getMdot() * dt;

	}

	public static void pitchKick(Stage stage) {
		double pitch = 0;//inputVars.getPitch();//0.065;//0.049;	// A higher value for pitch gives a more extreme pitch-kick
		double yaw = 0;//inputVars.getYaw();//-0.78;		// A positive yaw aims south, a negative yaw aims north. 
		double cs, sn;

		stage.gamma[0] = acos(cos(pitch) * cos(incl) - sin(pitch) * sin(yaw) * sin(incl));

		cs = sin(pitch) * cos(yaw) / sin(stage.gamma[0]);
		sn = (sin(pitch) * sin(yaw) * cos(incl) + cos(pitch) * sin(incl)) / sin(stage.gamma[0]);

		stage.gamma[1] = atan2(sn, cs);

		stage.gamma[1] -= (PI / 2 - lon);
	}

	/*
	 *	Flying prograde
	 */
	public static void gravityTurn(Stage stage) {
		stage.gamma[0] = PI - stage.alpha[0];
		stage.gamma[1] = PI + stage.alpha[1];
	}

	public static double gravityAtRadius(double radius) {
		if (radius < 0.0) {
			throw new IllegalArgumentException();
		}
		return gravConstant * massOfEarth / pow(radius, 2);
	}
	
	public static double densityAtAltitude(double altitude) {
		if ((int) (altitude) < 0.0) {
			throw new IllegalArgumentException();
		}
		return 1.21147 * Math.exp(altitude * -1.12727e-4);
	}

	public static double pressureAtAltitude(double altitude) {
		if ((int) (altitude) < 0.0) {
			throw new IllegalArgumentException();
		}
		return -517.18 * Math.log(0.012833 * Math.log(6.0985e28 * altitude + 2.0981e28));
	}

	

}
