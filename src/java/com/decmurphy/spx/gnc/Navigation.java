package com.decmurphy.spx.gnc;

import com.decmurphy.spx.util.Correction;
import com.decmurphy.spx.vehicle.Stage;
import static com.decmurphy.utils.Globals.dt;
import static com.decmurphy.utils.Globals.earthVel;
import static com.decmurphy.utils.Globals.radiusOfEarth;
import com.decmurphy.utils.Maths.CartesianCoordinates;
import com.decmurphy.utils.Maths.CartesianVelocity;
import com.decmurphy.utils.Maths.SphericalCoordinates;
import com.decmurphy.utils.Maths.SphericalVelocity;
import static com.decmurphy.utils.Maths.magnitudeOf;
import static com.decmurphy.utils.Physics.densityAtAltitude;
import static com.decmurphy.utils.Physics.gravityAtRadius;
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

	public static void leapfrogFirstStep(Stage s) {
		
		s.new_longitude = s.old_longitude = s.getParent().getMission().getLaunchSite().getLong();
		s.new_inclination = s.old_inclination = s.getParent().getMission().getLaunchSite().getIncl();

		s.beta[0] = 0.0;
		s.gamma[0] = 0.0;

		try {
			s.beta[0] = s.getEffectiveMass()*gravityAtRadius(radiusOfEarth + s.alt());
			s.gamma[0] = s.getThrustAtAltitude(s.alt());
		} catch (IllegalArgumentException e) {
		}

		s.force[0] = s.gamma[0]*sin(s.gamma[1])*cos(s.gamma[2]) + s.beta[0]*sin(s.beta[1])*cos(s.beta[2]);
		s.force[1] = s.gamma[0]*sin(s.gamma[1])*sin(s.gamma[2]) + s.beta[0]*sin(s.beta[1])*sin(s.beta[2]);
		s.force[2] = s.gamma[0]*cos(s.gamma[1])                 + s.beta[0]*cos(s.beta[1]);

		s.accel[0] = s.force[0]/s.getEffectiveMass();
		s.accel[1] = s.force[1]/s.getEffectiveMass();
		s.accel[2] = s.force[2]/s.getEffectiveMass();

    // sx = sin(2) ; vx = cos(2)
    // sy = cos(2) ; vy = -sin(2)
		s.absVel[0] += s.accel[0]*dt/2;
		s.absVel[1] += s.accel[1]*dt/2;
		s.absVel[2] += s.accel[2]*dt/2;

		s.relVel[0] += s.accel[0]*dt/2;
		s.relVel[1] += s.accel[1]*dt/2;
		s.relVel[2] += s.accel[2]*dt/2;

		s.isMoving = true;

	}

	/*
	 *	This function is where the magic happens. Evaluate forces on vehicle, update
   *  accelerations, velocities, positions.	Update angles. Execute gravity turn
   *  starting at T+0:55. And don't forget to remove the appropriate amount of mass
   *  from the system.
	 */
	public static void leapfrogStep(Stage s) {

		s.pos[0] += s.absVel[0]*dt;
		s.pos[1] += s.absVel[1]*dt;
		s.pos[2] += s.absVel[2]*dt;
		s.S = magnitudeOf(s.pos);

		s.relPos[0] += s.relVel[0]*dt;
		s.relPos[1] += s.relVel[1]*dt;
		s.relPos[2] += s.relVel[2]*dt;
		s.relS = magnitudeOf(s.relPos);
    
    executeBoundaryConditions(s);

		s.force[0] = s.gamma[0]*sin(s.gamma[1])*cos(s.gamma[2]) + s.alpha[0]*sin(s.alpha[1])*cos(s.alpha[2])	+ s.beta[0]*sin(s.beta[1])*cos(s.beta[2]);
		s.force[1] = s.gamma[0]*sin(s.gamma[1])*sin(s.gamma[2]) + s.alpha[0]*sin(s.alpha[1])*sin(s.alpha[2])	+ s.beta[0]*sin(s.beta[1])*sin(s.beta[2]);
		s.force[2] = s.gamma[0]*cos(s.gamma[1])                 + s.alpha[0]*cos(s.alpha[1])                  + s.beta[0]*cos(s.beta[1]);

		s.accel[0] = s.force[0]/s.getEffectiveMass();
		s.accel[1] = s.force[1]/s.getEffectiveMass();
		s.accel[2] = s.force[2]/s.getEffectiveMass();

    /*
      If vehicle hasn't launched, then the stage's velocity matches the Earth's
      rotation.
    */
    if(!s.getParent().hasLaunched()) {
      s.absVel[0] =  earthVel*sin(s.beta[1])*sin(s.beta[2]);
      s.absVel[1] = -earthVel*sin(s.beta[1])*cos(s.beta[2]);
      s.absVel[2] = 0.0;
    }
    else {
      s.absVel[0] += s.accel[0]*dt;
      s.absVel[1] += s.accel[1]*dt;
      s.absVel[2] += s.accel[2]*dt;
    }
    s.VA = magnitudeOf(s.absVel);
		
    /*
      Alright this is a bit weird. I want to find the velocity of the stage
      relative to the Earth.
    
      I convert absVel to spherical coordinates first because it's easier to
      correct for the Earth's rotation in spherical.
    
      baseVel is the spherical velocity of the stage corrected for the Earth's rotation.
      But we want it in Cartesian coordinates. Converting back isn't so simple because
      converting between cartesian and spherical velocity depends on the position.
      So if we want the stage's velocity vector for applying a drag force, we use
      the absolute position to convert back. If we want it for display purposes,
      we use the relative position to convert back.
    
      You might need to think about this one a bit. Think about the case where the
      absolute position is on the far side of the earth to the relative position
      (which would happen if you sat at the launchpad for 12 hours before launching)
    */
		s.baseVel = new CartesianVelocity(s.absVel)
										.convertToSpherical(new CartesianCoordinates(s.pos))
										.rotateEarth()
				            .getValues();
	
    s.dragVel = new SphericalVelocity(s.baseVel)
										.convertToCartesian(new CartesianCoordinates(s.pos).convertToSpherical())
										.getValues();
		
    s.relVel = new SphericalVelocity(s.baseVel)
										.convertToCartesian(new CartesianCoordinates(s.relPos).convertToSpherical())
										.getValues();

    s.VR = magnitudeOf(s.relVel);
    s.DVR = magnitudeOf(s.dragVel);
    
    updateVectors(s);
    removeMassFromSystem(s);

	}

	/*
	 *	3D is hard. Honestly it's like an order of magnitude harder than 2D.
	 *	My current method for any kind of course correction is to transform the 
   *  stage's orientation into an easier-to-work-with coordinate system, do
   *  the operation, and then transform back. For the pitch kick I can skip the
   *  first step cause I know I'm starting from vertical pitch. So rotate
   *  (pitch, yaw) by theta degrees about the y-axis and then rotate by phi
   *  degrees about the z-axis.
	 *
	 *	For Cape Canaveral, this is rotating (pitch, yaw) by 61.51 degrees about y,
   *  and then by -80.58 degrees about z.	Voila. That's your heading after pitch-kick.
   *  Hint: Use the right-hand rule!
	 */
	public static void pitchKick(Stage s, double pitch, double yaw) {

		// A higher value for pitch gives a more extreme pitch-kick
		// A positive yaw aims south, a negative yaw aims north.
		double incl = acos(s.pos[2]/radiusOfEarth);
		double lon = atan2(s.pos[1], s.pos[0]);

    SphericalCoordinates attitudeS = new SphericalCoordinates(s.S, pitch, yaw);
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

    double incl = atan2(sqrt(s.pos[0]*s.pos[0] + s.pos[1]*s.pos[1]), s.pos[2]);
    double lon = atan2(s.pos[1], s.pos[0]);
    
    // arr is in Spherical form already, but we create an explicit object here
    // to make rotations and conversions easier. 
    CartesianCoordinates newVector = 
            new SphericalCoordinates(arr[0], arr[1], arr[2])
            .convertToCartesian();
    
    int r;
    switch (dir) {
      case BACKWARD: r = -1; newVector.rotateZ(r*lon); newVector.rotateY(r*incl); break;
      case FORWARD: r = 1; newVector.rotateY(r*incl); newVector.rotateZ(r*lon); break;
      default: throw new IllegalArgumentException("Invalid Transform direction");
    }

    // Not zero based for the same reasons as the pitch kick
    for(int i=1;i<arr.length;i++)
      arr[i] = newVector.convertToSpherical().get(i);
 
	}

	public static void applyCorrection(Stage s, Correction c, double param) {

		if(s.gamma[0]>0.0) {
			vectorTransform(s, s.gamma, Transform.BACKWARD);
    
      /*
        Doing pitch kick here doesn't work as it resets one of the parameters to zero.
        It's an *absolute* change as opposed to a *relative* change.
      */

		  switch(c) {
			  case PITCH:
				  s.gamma[1] = PI/2 - param;
				  break;
			  case YAW:
				  s.gamma[2] = PI/2 + param;
				  break;
			  case THROTTLE:
				  s.setThrottle(param/100.0);
				  break;
			  default:
				  break;
      }
		
			vectorTransform(s, s.gamma, Transform.FORWARD);
		}

	}

	/*
	 *	Flying prograde
	 */
	public static void gravityTurn(Stage s) {

		if (s.extraBurnIsUnderway()) {    // Fly retrograde
			s.gamma[1] = s.alpha[1];
			s.gamma[2] = s.alpha[2];
		} else {                          // Gravity turn
			s.gamma[1] = PI - s.alpha[1];
			s.gamma[2] = PI + s.alpha[2];
		}
	}
  
  private static void executeBoundaryConditions(Stage s) {
    
		try {
			s.setQ(0.5*densityAtAltitude(s.alt())*s.DVR*s.DVR);
			if (s.getPropMass() < s.getMinimumFuelLimit())
				s.setThrottle(0.0);

			if (s.isMoving) {
				s.alpha[0] = s.getQ()*s.getAeroProp("Cd")*s.getAeroProp("XA");
				s.gamma[0] = s.getThrustAtAltitude(s.alt());
				s.beta[0] = s.getEffectiveMass()*gravityAtRadius(radiusOfEarth + s.alt());
			} else {
				s.alpha[0] = s.gamma[0] = s.beta[0] = 0.0;
			}
		} catch (IllegalArgumentException e) {
		}

		/*
		 *	If a crash happens (i.e if distance from earth's centre < earth's radius)
     *  then turn off engines. 	Introduce an upwards reaction force (Newton's 3rd
     *  law) which effectively cancels out gravity.	There should be no more
     *  movement after this point.
		 *
		 *	I had to introduce the 'if(s.clock())' statement because for a split
     *  second at the very start, the stage falls before it rises. I'll find a
     *  more elegant solution to this problem soon.
		 */
		if(!s.isThrottleTest()) {
			if (s.alt() < 0) {
				if (s.landingBurnIsUnderway()) {
					System.out.printf("T%+7.2f\t%.32s\n", s.clock(), "Crash/Landing");
				}

				if(s.clock() > 5.0)
					s.setLanded(true);
      
				for (int i = 0; i < 3; i++) {
					s.pos[i] -= s.absVel[i]*dt;
					s.relPos[i] -= s.relVel[i]*dt;
					s.absVel[i] = 0.0;
				}
	  		s.relS = magnitudeOf(s.relPos);
				s.S = magnitudeOf(s.pos);
				if (s.clock() > 100.0) {
					s.setThrottle(0.0);
				}
				s.setLandingBurnIsUnderway(false);
				s.isMoving = false;

				s.gamma[0] = 0.0;
				s.beta[0] = 0.0;
				s.alpha[0] = 0.0;
			}
		}
  }
  
  private static void updateVectors(Stage s) {
    
		s.alpha[1] = PI - atan2(sqrt(s.dragVel[0]*s.dragVel[0] + s.dragVel[1]*s.dragVel[1]), s.dragVel[2]);
		s.alpha[2] = PI + atan2(s.dragVel[1], s.dragVel[0]);

		s.beta[1] = PI - atan2(sqrt(s.pos[0]*s.pos[0] + s.pos[1]*s.pos[1]), s.pos[2]);
		s.beta[2] = PI + atan2(s.pos[1], s.pos[0]);

  }

  private static void removeMassFromSystem(Stage s) {
    double dm = s.getThrottle()*s.getNumEngines()*s.getEngine().getMdot()*dt;
    double newMass = s.getPropMass() - dm;
 		s.setPropMass(newMass);
  }
}
