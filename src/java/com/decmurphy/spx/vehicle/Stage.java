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
	 *	All vectors are given in spherical coordinates and hence are given by three
   *  parameters - the magnitude and the polar/azimuthal angles.
	 *
	 *	angle[1] is the polar angle - measured from the z-axis.
	 *	Range = [0:pi]
	 *	 - i.e 	above the north pole = 0 ; above the equator = pi/2 ; above the south pole = pi
	 *
	 *	angle[2] is the azimuthal angle (longitude) going west from Greenwich.
	 *	Range [-pi:pi) or [0:2pi) (just be consistent and keep 0 at Greenwich)
	 *	 - i.e	Cape Canaveral = 85.5 degrees (whatever that is in radians) ; Hong Kong = -114.2 (or 245.8) degrees
	 *
	 *	I would highly recommend looking up how to convert between cartesian (x,y,z) and spherical coordinates
	 *	before going any further. It's gonna come up a lot and I'm not gonna be explaining it every time!
	 *
	 */
	public double[] alpha,	// attitude vector  (drag acts through this angle)
									beta,		// position	vector  (gravity acts through this one - points towards earth's centre)
									gamma;  // thrust vector    (guess what acts through this one)
	/*
	 *	These ones are in cartesian coordinates. It's started off easier to visualize but I never thought
	 *	about whether they should be cartesian or spherical. Should I put these in spherical coordinates too?
	 *	I think I'd need to transform my coordinate system every timestep which I am absolutely not doing
	 */
	public double[] pos,	  // Absolute position
                  relPos, // Relative position for results plots
									absVel, // absolute velocity	(Starts at earth velocity at launch pad. Gives a nice boost closer to equator)
			            baseVel,//
			            dragVel,// the velocity vector that should be used in the drag equation
									relVel, // relative velocity	(relative to earth's surface. Starts at 0)
									accel,	// acceleration
									force;	// force

	public double relS, S, VR, DVR, VA;	// magnitudes of vectors
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

		this.S = this.relS = this.VR = this.DVR = this.VA = 0.0;

		this.alpha = new double[3];
		this.beta = new double[3];
		this.gamma = new double[3];

		this.pos = new double[3];
		this.relPos = new double[3];
		this.absVel = new double[3];
		this.baseVel = new double[3];
		this.dragVel = new double[3];
		this.relVel = new double[3];
		this.accel = new double[3];
		this.force = new double[3];

		this.parent = null;
		this.completedOrbits = 0;
		this.new_longitude = this.old_longitude = 0.0;
		this.new_inclination = this.old_inclination = 0.0;

		this.setCoordinates(0.0, 0.0);
	}

	public Stage(Stage s) {

		this.name = "CopyStage";
		this.setThrottle(s.getThrottle());
		this.setLegs(s.hasLegs());
		this.isMoving = s.isMoving;
		this.beforeSep = s.beforeSep;
    this.setLanded(s.isLanded());
		this.setExtraBurnIsUnderway(s.extraBurnIsUnderway());
		this.setLandingBurnIsUnderway(s.landingBurnIsUnderway());
		this.setAeroProperties(s.getAeroProp("r"), s.getAeroProp("Cd"));
		this.setDryMass(s.getDryMass());
		this.setPropMass(s.getPropMass());
		this.setMinimumFuelLimit(s.getMinimumFuelLimit());

		this.setEngines(s.getNumEngines(), s.getEngine());

		this.S = s.S;
    this.relS = s.relS;
		this.VR = s.VR;
		this.DVR = s.DVR;
		this.VA = s.VA;
		
		this.setAdditionalMass(s.getAdditionalMass());

		this.alpha = new double[]{s.alpha[0], s.alpha[1], s.alpha[2]};
		this.beta = new double[]{s.beta[0], s.beta[1], s.beta[2]};
		this.gamma = new double[]{s.gamma[0], s.gamma[1], s.gamma[2]};

		this.pos = new double[]{s.pos[0], s.pos[1], s.pos[2]};
		this.relPos = new double[]{s.relPos[0], s.relPos[1], s.relPos[2]};
		this.absVel = new double[]{s.absVel[0], s.absVel[1], s.absVel[2]};
		this.baseVel = new double[]{s.baseVel[0], s.baseVel[1], s.baseVel[2]};
		this.dragVel = new double[]{s.dragVel[0], s.dragVel[1], s.dragVel[2]};
		this.relVel = new double[]{s.relVel[0], s.relVel[1], s.relVel[2]};
		this.accel = new double[]{s.accel[0], s.accel[1], s.accel[2]};
		this.force = new double[]{s.force[0], s.force[1], s.force[2]};

		this.setParent(s.getParent());
		this.completedOrbits = s.completedOrbits;
		this.new_longitude = s.new_longitude;
		this.old_longitude = s.old_longitude;
		this.new_inclination = s.new_inclination;
		this.old_inclination = s.old_inclination;
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
		System.arraycopy(stage.relPos, 0, this.relPos, 0, stage.relPos.length);
		System.arraycopy(stage.relVel, 0, this.relVel, 0, stage.relVel.length);
		System.arraycopy(stage.absVel, 0, this.absVel, 0, stage.absVel.length);

		System.arraycopy(stage.alpha, 0, this.alpha, 0, stage.alpha.length);
		System.arraycopy(stage.beta, 0, this.beta, 0, stage.beta.length);
		System.arraycopy(stage.gamma, 0, this.gamma, 0, stage.gamma.length);
	}

	/*
	 *	Output telemetry to file. Nothing interesting happens here.
	 */
	public void outputFile(String id, boolean eventsFile) {
		PrintWriter pw = null;

		try {
      String fileName = "/" + id + "_" + name + (eventsFile ? "_events.dat" : ".dat");
			File outputFile = new File(outputPath, fileName);
			pw = new PrintWriter(new FileWriter(outputFile, true));

			pw.printf("%6.2f\t%9.3f\t%9.3f\t%9.3f\t%8.3f\t%8.3f\t%5.3f\t%10.3f\t%10.3f\n",
				clock(), relPos[0]*1e-3, relPos[1]*1e-3, relPos[2]*1e-3, 
				alt()*1e-3, relVel(), getDownrangeDistance()*1e-3, Q*1e-3, getPropMass()*1e-3);
			
			if(eventsFile)
				pw.printf("\n");
      
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
	public final void setExtraBurnIsUnderway(boolean b) {
		this.extraBurn = b;
	}
	public boolean extraBurnIsUnderway() {
		return extraBurn;
	}
	
	private boolean landingBurn;
	public final void setLandingBurnIsUnderway(boolean b) {
		this.extraBurn = b;
		this.landingBurn = b;
	}
	public boolean landingBurnIsUnderway() {
		return landingBurn;
	}
  
  public boolean landed;
  public final void setLanded(boolean b) {
    landed = b;
  }
  public boolean isLanded() {
    return landed;
  }

	private LaunchVehicle parent;
	public final void setParent(LaunchVehicle lv) {
		parent = lv;
	}
	public LaunchVehicle getParent() {
		return parent;
	}

	private int completedOrbits;
	public double new_longitude, old_longitude;
	public double new_inclination, old_inclination;
	public int completedOrbits(boolean goingToPolarOrbit) {
		if(goingToPolarOrbit) {
			new_inclination = acos(this.pos[2]/this.S);

			if (isMoving && old_inclination < parent.getMission().LaunchSite().getIncl()
						&& new_inclination > parent.getMission().LaunchSite().getIncl()) {
				completedOrbits++;
			}
			old_inclination = new_inclination;
		}
		else {
			new_longitude = atan2(this.pos[1], this.pos[0]);

			if (old_longitude < parent.getMission().LaunchSite().getLong()
						&& new_longitude > parent.getMission().LaunchSite().getLong()) {
				completedOrbits++;
			}
			old_longitude = new_longitude;
		}
		return completedOrbits;
	}

	private Engine engine;
	public final void setEngines(int numEngines, Engine engine) {
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
	public final void setThrottle(double throttle) {
		this.throttle = throttle;
	}
	public double getThrottle() {
		return throttle;
	}
	
	private double minimumFuelLimit;	// Cut engines when fuel level drops below this
	public final void setMinimumFuelLimit(double fl) {
		this.minimumFuelLimit = fl;
	}
	public double getMinimumFuelLimit() {
		return minimumFuelLimit;
	}

	private boolean hasLegs;
	public final void setLegs(boolean hasLegs) {
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
	public final void setAeroProperties(double radius, double dragCoefficient) {
		this.Cd = dragCoefficient;
		this.radius = radius;
		this.XA = PI*radius*radius;
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
	protected final void setDryMass(double dryMass) {
		this.dryMass = dryMass;
	}
	public double getDryMass() {
		return dryMass;
	}

	private double propMass;	
	public final void setPropMass(double propMass) {
		this.propMass = propMass;
	}
	public double getPropMass() {
		return propMass;
	}

	public double getMass() {
		return this.getDryMass() + this.getPropMass();
	}

	private double additionalMass;
	public final void setAdditionalMass(double mass) {
		this.additionalMass = mass;
	}
	public double getAdditionalMass() {
		return additionalMass;
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
		
    // tempBeta is just beta from relPos instead of pos
		double[] tempBeta = new double[3];
		tempBeta[1] = PI - atan2(sqrt(relPos[0]*relPos[0] + relPos[1]*relPos[1]), relPos[2]);
		tempBeta[2] = PI + atan2(relPos[1], relPos[0]);
		
    double _longitude1 = PI - getParent().getMission().LaunchSite().getIncl();
    double _longitude2 = tempBeta[1];

    double psi1 = getParent().getMission().LaunchSite().getLong();
    double psi2 = tempBeta[2] - PI;

    return radiusOfEarth*acos(cos(_longitude1)*cos(_longitude2) + sin(_longitude1)*sin(_longitude2)*cos(psi1-psi2));
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

}
