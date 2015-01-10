package com.decmurphy.spx.vehicle;

import static com.decmurphy.spx.Globals.densityAtAltitude;
import static com.decmurphy.spx.Globals.dt;
import static com.decmurphy.spx.Globals.earthVel;
import static com.decmurphy.spx.Globals.gravityAtRadius;
import static com.decmurphy.spx.Globals.incl;
import static com.decmurphy.spx.Globals.lon;
import static com.decmurphy.spx.Globals.profile;
import static com.decmurphy.spx.Globals.radiusOfEarth;
import static com.decmurphy.spx.Globals.t;
import static com.decmurphy.spx.servlet.InterfaceServlet.outputPath;
import java.io.*;
import com.decmurphy.spx.engine.Engine;

public class Stage {

    private String name;
    private double numEngines;
    private Engine engine;
    private double throttle;

    protected boolean hasLegs;
    protected boolean isMoving;
    private double radius;
    private double dryMass;
    private double fuelCapacity;
    private double propMass;

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
    private double[] alpha, // angle of attack 		(drag acts through this angle)
            beta, // angle of position	(gravity acts through this one)
            gamma;          // angle of thrust		(guess what acts through this one)

    /*
     *	These ones are in cartesian coordinates. It's started off easier to visualize but I never thought
     *	about whether they should be cartesian or spherical. Should I put these in spherical coordinates too?
     *	I'll think about it. It would make the leapfrog	function a lot simpler, that's for sure.
     */
    private double[] pos, // cartesian position
            filepos, // (experimental)
            relVel, // relative velocity	(relative to earth's surface. Starts at 0)
            absVel, // absolute velocity	(Starts at earth velocity at launch pad. Gives a nice boost closer to equator)
            accel, // acceleration
            force;          // force

    private double S, VR, VA, A, M;	// magnitudes of position, relV, absV, acceleration, total mass
    private double Q, Cd, XA;		// aerodynamic pressure, drag coefficient, cross-sectional area of stage

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

        this.setCoordinates(0.0, 0.0);
    }

    protected void setCoordinates(double cLat, double cLong) {
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

    protected void leapfrogFirstStep() {
        try {
            thrustForce = getThrustAtAltitude(S - radiusOfEarth);
            gravityForce = M * gravityAtRadius(S);
        } catch (IllegalArgumentException e) {
        }

        force[0] = thrustForce * Math.sin(gamma[0]) * Math.sin(gamma[1]) + gravityForce * Math.sin(beta[0]) * Math.sin(beta[1]);
        force[1] = thrustForce * Math.sin(gamma[0]) * Math.cos(gamma[1]) + gravityForce * Math.sin(beta[0]) * Math.cos(beta[1]);
        force[2] = thrustForce * Math.cos(gamma[0]) + gravityForce * Math.cos(beta[0]);

        accel[0] = force[0] / M;
        accel[1] = force[1] / M;
        accel[2] = force[2] / M;

        absVel[0] = accel[0] * dt / 2 - earthVel * Math.sin(beta[0]) * Math.cos(beta[1]);
        absVel[1] = accel[1] * dt / 2 - earthVel * Math.sin(beta[0]) * Math.sin(beta[1]);
        absVel[2] = accel[2] * dt / 2;

        relVel[0] = 0;
        relVel[1] = 0;
        relVel[2] = 0;

        S = Math.sqrt(pos[0] * pos[0] + pos[1] * pos[1] + pos[2] * pos[2]);
        A = Math.sqrt(accel[0] * accel[0] + accel[1] * accel[1] + accel[2] * accel[2]);

        this.isMoving = true;
    }

    /*
     *	This function is where the magic happens. Evaluate forces on vehicle, update accelerations, velocities, positions.
     *	Update angles.
     *	Execute gravity turn starting at T+0:55
     *	And don't forget to remove the appropriate amount of mass from the system.
     */
    protected void leapfrogStep() {

        try {
            Q = 0.5 * densityAtAltitude(S - radiusOfEarth) * VR * VR;

            if (this.isMoving) {
                dragForce = Q * Cd * XA;
                thrustForce = getThrustAtAltitude(S - radiusOfEarth);
                gravityForce = M * gravityAtRadius(S);
            } else {
                dragForce = thrustForce = gravityForce = 0.0;
            }
        } catch (IllegalArgumentException e) {
            if (VR < 5) {
                System.out.printf(this.name + " Touchdown wooooooo\n");
                //System.exit(0);
            } else {
                System.out.printf("Looks like your " + this.name + " crashed m8.\n");
                //System.exit(1);
            }

        }

        pos[0] += absVel[0] * dt;
        pos[1] += absVel[1] * dt;
        pos[2] += absVel[2] * dt;

        filepos[0] += relVel[0] * dt;
        filepos[1] += relVel[1] * dt;
        filepos[2] += relVel[2] * dt;

        S = Math.sqrt(pos[0] * pos[0] + pos[1] * pos[1] + pos[2] * pos[2]);

        /*
         *	If a crash happens (i.e if distance from earth's centre < earth's radius) then turn off engines.
         *	Introduce an upwards reaction force (Newton's 3rd law) which effectively cancels out gravity.
         *	There should be no more movement after this point.
         *
         *	I had to introduce the 'if(onBoardClock)' statement because for a split second at the very start,
         *	the stage falls before it rises. I'll find a more elegant solution to this problem soon.
         */
        if (S < radiusOfEarth) {
            int i;
            for (i = 0; i < 3; i++) {
                pos[i] -= absVel[i] * dt;
                filepos[i] -= relVel[i] * dt;
                absVel[i] = 0.0;
            }
            S = Math.sqrt(pos[0] * pos[0] + pos[1] * pos[1] + pos[2] * pos[2]);

//			if(F9.onBoardClock > 1.0) setThrottle(0.0);
            gravityForce = 0.0;
        }

        force[0] = thrustForce * Math.sin(gamma[0]) * Math.sin(gamma[1])
                + dragForce * Math.sin(alpha[0]) * Math.sin(alpha[1])
                + gravityForce * Math.sin(beta[0]) * Math.sin(beta[1]);
        force[1] = thrustForce * Math.sin(gamma[0]) * Math.cos(gamma[1])
                + dragForce * Math.sin(alpha[0]) * Math.cos(alpha[1])
                + gravityForce * Math.sin(beta[0]) * Math.cos(beta[1]);
        force[2] = thrustForce * Math.cos(gamma[0])
                + dragForce * Math.cos(alpha[0])
                + gravityForce * Math.cos(beta[0]);

        accel[0] = force[0] / M;
        accel[1] = force[1] / M;
        accel[2] = force[2] / M;
        A = Math.sqrt(accel[0] * accel[0] + accel[1] * accel[1] + accel[2] * accel[2]);

        absVel[0] += accel[0] * dt;
        absVel[1] += accel[1] * dt;
        absVel[2] += accel[2] * dt;
        VA = Math.sqrt(absVel[0] * absVel[0] + absVel[1] * absVel[1] + absVel[2] * absVel[2]);

        relVel[0] = absVel[0] + earthVel * Math.sin(beta[0]) * Math.cos(beta[1]);
        relVel[1] = absVel[1] + earthVel * Math.sin(beta[0]) * Math.sin(beta[1]);
        relVel[2] = absVel[2];
        VR = Math.sqrt(relVel[0] * relVel[0] + relVel[1] * relVel[1] + relVel[2] * relVel[2]);

        alpha[0] = Math.PI - Math.atan2(Math.sqrt(relVel[0] * relVel[0] + relVel[1] * relVel[1]), relVel[2]);
        alpha[1] = Math.PI + Math.atan2(relVel[0], relVel[1]);

        beta[0] = Math.PI - Math.atan2(Math.sqrt(pos[0] * pos[0] + pos[1] * pos[1]), pos[2]);
        beta[1] = Math.PI + Math.atan2(pos[0], pos[1]);

        propMass -= throttle * numEngines * engine.getMdot() * dt;

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
    public void pitchKick() {
        double pitch = profile.getPitch();//0.065;//0.049;	// A higher value for pitch gives a more extreme pitch-kick
        double yaw = profile.getYaw();//-0.78;		// A positive yaw aims south, a negative yaw aims north. 
        double cs, sn;

        gamma[0] = Math.acos(Math.cos(pitch) * Math.cos(incl) - Math.sin(pitch) * Math.sin(yaw) * Math.sin(incl));

        cs = Math.sin(pitch) * Math.cos(yaw) / Math.sin(gamma[0]);
        sn = (Math.sin(pitch) * Math.sin(yaw) * Math.cos(incl) + Math.cos(pitch) * Math.sin(incl)) / Math.sin(gamma[0]);

        gamma[1] = Math.atan2(sn, cs);

        gamma[1] -= (Math.PI / 2 - lon);
    }

    /*
     *	Flying prograde
     */
    protected void gravityTurn() {
        gamma[0] = Math.PI - alpha[0];
        gamma[1] = Math.PI + alpha[1];
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

//		this.OnBoardClock = 0.0 + stage.onBoardClock;
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
    public void outputFile(String id) {
        PrintWriter pw = null;

        try {
            File outputFile = new File(outputPath, "/" + id + "_" + name + ".dat");
            pw = new PrintWriter(new FileWriter(outputFile, true));

            pw.printf("%6.2f\t%9.3f\t%9.3f\t%9.3f\t%8.3f\t%8.3f\t%5.3f\t%10.3f\n",
                    t, pos[0] * 1e-3, pos[1] * 1e-3, pos[2] * 1e-3, (S - radiusOfEarth) * 1e-3, VA, throttle, M);

        } catch (IOException e) {
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
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

    /*
     *	Getter Functions
     */
    protected boolean hasLegs() {
        return hasLegs;
    }

    protected double getRadius() {
        return radius;
    }

    protected double getDryMass() {
        return dryMass;
    }

    public double getPropMass() {
        return propMass;
    }

    protected double getMass() {
        return this.getDryMass() + this.getPropMass();
    }

    protected double getFuelCapacity() {
        return fuelCapacity;
    }

    private double getThrustAtAltitude(double altitude) {
        return throttle * numEngines * engine.getThrustAtAltitude(altitude);
    }

    public double alt() {
        return S - radiusOfEarth;
    }

    public double vel() {
        return VR;
    }

}
