package com.decmurphy.spx.vehicle;

import static com.decmurphy.spx.Globals.incl;
import static com.decmurphy.spx.Globals.lon;
import static com.decmurphy.spx.Globals.radiusOfEarth;
import static com.decmurphy.spx.servlet.InterfaceServlet.outputPath;
import java.io.*;
import com.decmurphy.spx.engine.Engine;
import static java.lang.Math.atan2;

public class Stage {

	public String name;
	public double numEngines;
	private Engine engine;
	public double throttle;

	protected boolean hasLegs;
	public boolean isMoving;
	private double radius;
	private double dryMass;
	private double fuelCapacity;
	public double propMass;
	public double onBoardClock;
	
	private int completedOrbits;
	private double newtheta, oldtheta;

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
	public double[] alpha, // angle of attack 		(drag acts through this angle)
					beta, // angle of position	(gravity acts through this one)
					gamma;          // angle of thrust		(guess what acts through this one)

	/*
	 *	These ones are in cartesian coordinates. It's started off easier to visualize but I never thought
	 *	about whether they should be cartesian or spherical. Should I put these in spherical coordinates too?
	 *	I'll think about it. It would make the leapfrog	function a lot simpler, that's for sure.
	 */
	public double[] pos, // cartesian position
					filepos, // (experimental)
					relVel, // relative velocity	(relative to earth's surface. Starts at 0)
					absVel, // absolute velocity	(Starts at earth velocity at launch pad. Gives a nice boost closer to equator)
					accel, // acceleration
					force;          // force

	public double S, VR, VA, A, M;	// magnitudes of position, relV, absV, acceleration, total mass
	public double Q, Cd, XA;		// aerodynamic pressure, drag coefficient, cross-sectional area of stage

	private double dragForce, thrustForce, gravityForce;

	public Stage(String name) {
		this.name = name;

		this.throttle = 0.0;
		this.hasLegs = false;
		this.isMoving = false;
		this.radius = 0.0;
		this.dryMass = 0.0;
		this.propMass = 0.0;

		this.S = this.VR = this.VA = this.A = 0.0;

		this.alpha = new double[2];
		this.beta = new double[2];
		this.gamma = new double[2];

		this.pos = new double[3];
		this.filepos = new double[3];
		this.absVel = new double[3];
		this.relVel = new double[3];
		this.accel = new double[3];
		this.force = new double[3];
		
		this.completedOrbits = 0;
		this.newtheta = this.oldtheta = 0.0;

		this.setCoordinates(0.0, 0.0);
	}

	final void setCoordinates(double cLat, double cLong) {
		pos[0] = radiusOfEarth * Math.sin(cLat) * Math.sin(cLong);
		pos[1] = radiusOfEarth * Math.sin(cLat) * Math.cos(cLong);
		pos[2] = radiusOfEarth * Math.cos(cLat);

		filepos[0] = radiusOfEarth * Math.sin(cLat) * Math.sin(cLong);
		filepos[1] = radiusOfEarth * Math.sin(cLat) * Math.cos(cLong);
		filepos[2] = radiusOfEarth * Math.cos(cLat);

		S = radiusOfEarth;
 
		/*
		 *	gravity angle always points towards the centre of the planet
		 */
		beta[0] = Math.PI - Math.atan2(Math.sqrt(pos[0] * pos[0] + pos[1] * pos[1]), pos[2]);
		beta[1] = Math.PI + Math.atan2(pos[0], pos[1]);

		/*
		 *	thrust angle starts off pointing straight up
		 */
		gamma[0] = Math.PI - beta[0];	//Math.atan2(Math.sqrt(pos[0]*pos[0] + pos[1]*pos[1]), pos[2]);
		gamma[1] = Math.PI + beta[1];	//Math.atan2(pos[0], pos[1]);
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
     *	3D is hard. Honestly it's like an order of magnitude harder than 2D.
     *	My current method for any kind of operation is to transform it into an easier-to-work-with coordinate system,
     *	do the operation, and then transform back. For the pitch kick I can skip the first step. So rotate (pitch, yaw) by 'pi/2 - incl'
     *	degrees about the y-axis and then rotate by 'pi/2 - longitude' degrees about the z-axis.
     *
     *	For Cape Canaveral, this is rotating (pitch, yaw) by 61.51 degrees about y, and then by 9.42 degrees about z.
     *	Voila. That's your heading after pitch-kick.
     */
    public void pitchKick(double pitch, double yaw) {

		// A higher value for pitch gives a more extreme pitch-kick
		// A positive yaw aims south, a negative yaw aims north. 
        double cs, sn;

        gamma[0] = Math.acos(Math.cos(pitch) * Math.cos(incl) - Math.sin(pitch) * Math.sin(yaw) * Math.sin(incl));

        cs = Math.sin(pitch) * Math.cos(yaw) / Math.sin(gamma[0]);
        sn = (Math.sin(pitch) * Math.sin(yaw) * Math.cos(incl) + Math.cos(pitch) * Math.sin(incl)) / Math.sin(gamma[0]);

        gamma[1] = Math.atan2(sn, cs);

        gamma[1] -= (Math.PI / 2 - lon);
    }

	/*
	 *	Output telemetry to file. Nothing interesting happens here.
	 */
	public void outputFile(String id) {
		PrintWriter pw = null;

		try {
			File outputFile = new File(outputPath, "/" + id + "_" + name + ".dat");
			pw = new PrintWriter(new FileWriter(outputFile, true));

			pw.printf("%6.2f\t%9.3f\t%9.3f\t%9.3f\t%8.3f\t%8.3f\t%5.3f\t%10.3f\n",
							onBoardClock, pos[0] * 1e-3, pos[1] * 1e-3, pos[2] * 1e-3, (S - radiusOfEarth) * 1e-3, VA, throttle, M);

		} catch (IOException e) {
		} finally {
			if (pw != null) {
				pw.close();
			}
		}
	}
	
	public int completedOrbits() {
		newtheta = atan2(this.pos[1], this.pos[0]);
		
		if(newtheta < oldtheta)
			completedOrbits++;
		oldtheta = newtheta;
		
		return completedOrbits;
	}

	protected void setEngines(int numEngines, Engine engine) {
		this.numEngines = numEngines;
		this.engine = engine;
	}

	protected void setThrottle(double throttle) {
		this.throttle = throttle;
	}

	protected void setLegs(boolean hasLegs) {
		this.hasLegs = hasLegs;
		if (hasLegs) {
			this.dryMass += 2000;
		}
	}

	protected void setAeroProperties(double radius, double dragCoefficient) {
		this.Cd = dragCoefficient;

		this.radius = radius;
		this.XA = Math.PI * radius * radius;
	}

	protected void setDryMass(double dryMass) {
		this.dryMass = dryMass;
	}

	protected void setPropMass(double propMass) {
		this.propMass = propMass;
	}

	protected void setMass(double mass) {
		this.M = mass;
	}

	protected void setFuelCapacity(double fuelCapacity) {
		this.fuelCapacity = fuelCapacity;
	}

	public void setPos(double pos) {
		S = pos;
	}

	public void setAccel(double accel) {
		A = accel;
	}
	
	/*
	 *	Getter Functions
	 */
	protected double getRadius() {
		return radius;
	}

	protected double getDryMass() {
		return dryMass;
	}

	public double getPropMass() {
		return propMass;
	}

	public double getMass() {
		return this.getDryMass() + this.getPropMass();
	}

	protected double getFuelCapacity() {
		return fuelCapacity;
	}
	
	public Engine getEngine() {
		return engine;
	}
	
	public double getThrustAtAltitude(double altitude) {
		return throttle * numEngines * engine.getThrustAtAltitude(altitude);
	}

	public double alt() {
		return S - radiusOfEarth;
	}

	public double vel() {
		return VR;
	}
	
	protected boolean hasLegs() {
		return hasLegs;
	}

}
