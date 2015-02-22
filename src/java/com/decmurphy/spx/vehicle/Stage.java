package com.decmurphy.spx.vehicle;

import com.decmurphy.spx.engine.Engine;
import static com.decmurphy.spx.servlet.InterfaceServlet.outputPath;
import static com.decmurphy.utils.Globals.earthVel;
import static com.decmurphy.utils.Globals.radiusOfEarth;
import static com.decmurphy.utils.Maths.magnitudeOf;
import static com.decmurphy.utils.Physics.gravityAtRadius;
import java.io.*;
import static java.lang.Math.PI;
import static java.lang.Math.acos;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

public class Stage {

	/*
	 *	All angles are given in spherical coordinates and hence are given by three parameters - the radius and the polar/azimuthal angles.
	 *	The radius is the same for all angles at any given time since a stage can't be at two altitudes at once.
	 *	So we only care about the two angles.
	 *
	 *	angle[0] is the polar angle - measured from the z-axis.
	 *	Range = [0:pi]
	 *	 - i.e 	above the north pole = 0 ; above the equator = pi/2 ; above the south pole = pi
	 *
	 *	angle[1] is the azimuthal angle (longitude) going west from Greenwich.
	 *	Range [-pi:pi) or [0:2pi) (just be consistent and keep 0 at Greenwich)
	 *	 - i.e	Cape Canaveral = 85.5 degrees (whatever that is in radians) ; Hong Kong = -114.2 (or 245.8) degrees
	 *
	 *	I would highly recommend looking up how to convert between cartesian (x,y,z) and spherical coordinates
	 *	before going any further. It's gonna come up a lot and I'm not gonna be explaining it every time!
	 *
	 */
	public double[] alpha,	// angle of attack 		(drag acts through this angle)
									beta,		// angle of position	(gravity acts through this one - points towards earth's centre)
									gamma;  // angle of thrust		(guess what acts through this one)
	/*
	 *	These ones are in cartesian coordinates. It's started off easier to visualize but I never thought
	 *	about whether they should be cartesian or spherical. Should I put these in spherical coordinates too?
	 *	I'll think about it. It would make the leapfrog	function a lot simpler, that's for sure.
	 */
	public double[] tempPos, //
                  pos,	  // cartesian position
                  relPos, // For looking at first stage landing with coriolis on
									absVel, // absolute velocity	(Starts at earth velocity at launch pad. Gives a nice boost closer to equator)
									relVel, // relative velocity	(relative to earth's surface. Starts at 0)
									accel,	// acceleration
									force;	// force

	public double relS, S, VR, VA, A;	// magnitudes of position, relV, absV, acceleration
	public String name;
	public boolean isMoving;
	public boolean beforeSep;
	
	public Stage(String name) {
		this.name = name;

		this.throttle = 0.0;
		this.hasLegs = false;
		this.isMoving = false;
		this.beforeSep = true;
    this.landed = false;
		this.extraBurn = false;
		this.landingBurn = false;
		this.radius = 0.0;
		this.dryMass = 0.0;
		this.propMass = 0.0;

		this.S = this.relS = this.VR = this.VA = this.A = 0.0;

		this.alpha = new double[3];
		this.beta = new double[3];
		this.gamma = new double[3];

		this.tempPos = new double[3];
		this.pos = new double[3];
		this.relPos = new double[3];
		this.absVel = new double[3];
		this.relVel = new double[3];
		this.accel = new double[3];
		this.force = new double[3];

		this.parent = null;
		this.completedOrbits = 0;
		this.newtheta = this.oldtheta = 0.0;

		this.setCoordinates(0.0, 0.0);
	}

	public Stage(Stage s) {

		this.name = "CopyStage";
		this.throttle = s.throttle;
		this.hasLegs = s.hasLegs;
		this.isMoving = s.isMoving;
		this.beforeSep = s.beforeSep;
    this.landed = s.landed;
		this.extraBurn = s.extraBurn;
		this.landingBurn = s.landingBurn;
		this.radius = s.radius;
		this.dryMass = s.dryMass;
		this.propMass = s.propMass;

		this.engine = s.engine;
		this.numEngines = s.numEngines;

		this.S = s.S;
    this.relS = s.relS;
		this.VR = s.VR;
		this.VA = s.VA;
		this.A = s.A;
		
		this.Cd = s.Cd;
		this.XA = s.XA;
		this.additionalMass = s.additionalMass;

		this.alpha = new double[]{s.alpha[0], s.alpha[1], s.alpha[2]};
		this.beta = new double[]{s.beta[0], s.beta[1], s.beta[2]};
		this.gamma = new double[]{s.gamma[0], s.gamma[1], s.gamma[2]};

		this.tempPos = new double[]{s.tempPos[0], s.tempPos[1], s.tempPos[2]};
		this.pos = new double[]{s.pos[0], s.pos[1], s.pos[2]};
		this.relPos = new double[]{s.relPos[0], s.relPos[1], s.relPos[2]};
		this.absVel = new double[]{s.absVel[0], s.absVel[1], s.absVel[2]};
		this.relVel = new double[]{s.relVel[0], s.relVel[1], s.relVel[2]};
		this.accel = new double[]{s.accel[0], s.accel[1], s.accel[2]};
		this.force = new double[]{s.force[0], s.force[1], s.force[2]};

		this.parent = s.parent;
		this.completedOrbits = s.completedOrbits;
		this.newtheta = s.newtheta;
		this.oldtheta = s.oldtheta;
	}

	final void setCoordinates(double incl, double lon) {
		pos[0] = radiusOfEarth*sin(incl)*cos(lon);
		pos[1] = radiusOfEarth*sin(incl)*sin(lon);
		pos[2] = radiusOfEarth*cos(incl);
    S = magnitudeOf(pos);

		relPos[0] = pos[0];
		relPos[1] = pos[1];
		relPos[2] = pos[2];
    relS = magnitudeOf(relPos);

		/*
		 *	gravity angle always points towards the centre of the planet
		 */
    beta[0] = getEffectiveMass()*gravityAtRadius(radiusOfEarth + alt());
		beta[1] = PI - atan2(sqrt(pos[0]*pos[0] + pos[1]*pos[1]), pos[2]);
		beta[2] = PI + atan2(pos[1], pos[0]);   

		/*
		 *	thrust angle starts off pointing straight up
		 */
    gamma[0] = 0.0;
		gamma[1] = PI - beta[1];
		gamma[2] = PI + beta[2];
    
    absVel[0] =  earthVel*sin(beta[1])*sin(beta[2]);
    absVel[1] = -earthVel*sin(beta[1])*cos(beta[2]);
    absVel[2] = 0.0;
    VA = magnitudeOf(absVel);
    
    relVel[0] = 0.0;
    relVel[1] = 0.0;
    relVel[2] = 0.0;
    VR = magnitudeOf(relVel);

	}

	/*
	 *	This functions synchronizes two stages to prepare for separation. By using this function, I only have to worry about
	 *	one stage before separation. Then this copies the position/velocity/angles to the second stage.
	 */
	public void syncWith(Stage stage) {
		/*
		 *	Fucking Java. This function is the reason this code wasn't finished
		 *	months earlier. Who the fuck assigns references, honestly?
		 *	Fuck you, Java.
		 */

		System.arraycopy(stage.pos, 0, this.pos, 0, stage.pos.length);
		System.arraycopy(stage.relVel, 0, this.relVel, 0, stage.relVel.length);
		System.arraycopy(stage.absVel, 0, this.absVel, 0, stage.absVel.length);

		System.arraycopy(stage.alpha, 0, this.alpha, 0, stage.alpha.length);
		System.arraycopy(stage.beta, 0, this.beta, 0, stage.beta.length);
		System.arraycopy(stage.gamma, 0, this.gamma, 0, stage.gamma.length);
	}

	/*
	 *	Output telemetry to file. Nothing interesting happens here.
	 */
	public void outputFile(String id, boolean b) {
		PrintWriter pw = null;

		try {
			File outputFile = new File(outputPath, "/" + id + "_" + name + ".dat");
			pw = new PrintWriter(new FileWriter(outputFile, true));

			pw.printf("%6.2f\t%9.3f\t%9.3f\t%9.3f\t%8.3f\t%8.3f\t%5.3f\t%10.3f\t%10.3f\n",
				clock(), relPos[0]*1e-3, relPos[1]*1e-3, relPos[2]*1e-3, 
				(S-radiusOfEarth)*1e-3, VR, getDownrangeDistance()*1e-3, Q*1e-3, getPropMass()*1e-3);

      if(b) {
        File eventPointsFile = new File(outputPath, "/" + id + "_" + name + "_events.dat");
        pw = new PrintWriter(new FileWriter(eventPointsFile, true));

        pw.printf("%6.2f\t%9.3f\t%9.3f\t%9.3f\t%8.3f\t%8.3f\t%5.3f\t%10.3f\t%10.3f\n\n",
          clock(), relPos[0]*1e-3, relPos[1]*1e-3, relPos[2]*1e-3, 
					(S-radiusOfEarth)*1e-3, VR, getDownrangeDistance()*1e-3, Q*1e-3, getPropMass()*1e-3);
      }
		} catch (IOException e) {
		} finally {
			if (pw != null) {
				pw.close();
			}
		}
	}

	public double clock() {
		return parent.clock();
	}

	public void executeLandingBurn() {
		setLandingBurnIsUnderway(true);
	}
	
	private boolean extraBurn;
	public void setExtraBurnIsUnderway(boolean b) {
		this.extraBurn = b;
	}
	public boolean extraBurnIsUnderway() {
		return extraBurn;
	}
	
	private boolean landingBurn;
	public void setLandingBurnIsUnderway(boolean b) {
		this.extraBurn = b;
		this.landingBurn = b;
	}
	public boolean landingBurnIsUnderway() {
		return landingBurn;
	}
  
  public boolean landed;
  public void setLanded(boolean b) {
    landed = b;
  }
  public boolean isLanded() {
    return landed;
  }

	private LaunchVehicle parent;
	public void setParent(LaunchVehicle lv) {
		parent = lv;
	}
	public LaunchVehicle getParent() {
		return parent;
	}

	private int completedOrbits;
	private double newtheta, oldtheta;
	public int completedOrbits() {
		newtheta = atan2(this.pos[1], this.pos[0]);

		if (oldtheta < parent.getMission().LaunchSite().getLong()
						&& newtheta > parent.getMission().LaunchSite().getLong()) {
			completedOrbits++;
		}
		oldtheta = newtheta;

		return completedOrbits;
	}

	private Engine engine;
	public void setEngines(int numEngines, Engine engine) {
		this.numEngines = numEngines;
		this.engine = engine;
	}
	public Engine getEngine() {
		return engine;
	}
	
	private int numEngines;
	public void setEngines(int numEngines) {
		this.numEngines = numEngines;
	}
	public int getNumEngines() {
		return numEngines;
	}

	private double throttle;
	public void setThrottle(double throttle) {
		this.throttle = throttle;
	}
	public double getThrottle() {
		return throttle;
	}

	private boolean hasLegs;
	public void setLegs(boolean hasLegs) {
		this.hasLegs = hasLegs;
		if (hasLegs) {
			this.dryMass += 2000;
		}
	}
	public boolean hasLegs() {
		return hasLegs;
	}
	
	private boolean throttleTest = false;
	public void setThrottleTest(boolean b) {
		throttleTest = b;
	}
	public boolean isThrottleTest() {
		return throttleTest;
	}

	
	private double Q;
	public void setQ(double q) {
		Q = q;
	}
	public double getQ() {
		return Q;
	}

	private double radius, Cd, XA;
	public void setAeroProperties(double radius, double dragCoefficient) {
		this.Cd = dragCoefficient;
		this.radius = radius;
		this.XA = Math.PI * radius * radius;
	}
	public double getAeroProp(String s) {
		s = s.toLowerCase();
		switch(s) {
			case "r": return radius;
			case "cd": return getDragCoefficientAtAltitude(alt());
			case "xa": return XA;
			default:	throw new IllegalArgumentException("Unrecognised Aero Property");
		}
	}

	
	private double dryMass;
	protected void setDryMass(double dryMass) {
		this.dryMass = dryMass;
	}
	public double getDryMass() {
		return dryMass;
	}

	private double propMass;	
	public void setPropMass(double propMass) {
		this.propMass = propMass;
	}
	public double getPropMass() {
		return propMass;
	}

	public double getMass() {
		return this.getDryMass() + this.getPropMass();
	}

	private double additionalMass;
	public void setAdditionalMass(double mass) {
		this.additionalMass = mass;
	}
	public double getEffectiveMass() {
		return additionalMass + getMass();
	}

	private double fuelCapacity;
	public void setFuelCapacity(double fuelCapacity) {
		this.fuelCapacity = fuelCapacity;
	}
	public double getFuelCapacity() {
		return fuelCapacity;
	}
  
  public double getDownrangeDistance() {
    double theta1 = PI - getParent().getMission().LaunchSite().getIncl();
    double theta2 = beta[1];

    double psi1 = getParent().getMission().LaunchSite().getLong();
    double psi2 = beta[2] - PI;

    return radiusOfEarth*acos(cos(theta1)*cos(theta2) + sin(theta1)*sin(theta2)*cos(psi1-psi2));
  }

	//////////////////////////////////////////////////////////////////////////////

	public double getThrustAtAltitude(double altitude) {
		return getThrottle()*getNumEngines()*engine.getThrustAtAltitude(altitude);
	}
	
	public double getDragCoefficientAtAltitude(double altitude) {
		//		TODO:
		//		come up with some polynomial
		//		like http://www.braeunig.us/apollo/pics/cd2.gif
		//		from http://www.braeunig.us/apollo/saturnV.htm
		double machNumber = VR/(340.0-(altitude/260.0));
		return machNumber < 1 ? 0.2 :
						machNumber < 5 ? (11.0-machNumber)/20.0 :	// close enough function for sharp nose projectile
						0.3;
	}

	public double alt() {
		return S - radiusOfEarth;
	}

	public double absVel() {
		return VA;
	}
	
	public double relVel() {
		return VR;
	}

	public double[] vectorVel() {
		return relVel;
	}

}
