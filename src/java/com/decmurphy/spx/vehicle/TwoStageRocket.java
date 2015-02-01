package com.decmurphy.spx.vehicle;

import static com.decmurphy.spx.Globals.dt;
import static com.decmurphy.spx.Globals.gravConstant;
import static com.decmurphy.spx.Globals.massOfEarth;
import static com.decmurphy.spx.Globals.radiusOfEarth;
import com.decmurphy.spx.event.Event;
import static com.decmurphy.spx.gnc.HoverSlam.updateLandingThrottle;
import com.decmurphy.spx.gnc.Navigation;
import com.decmurphy.spx.launchsite.LaunchSite;
import com.decmurphy.spx.mission.Mission;
import com.decmurphy.spx.payload.Payload;
import com.decmurphy.spx.profile.Profile;
import static java.lang.Math.sqrt;

public abstract class TwoStageRocket implements LaunchVehicle {

	protected Payload payload;
	public Stage[] mStage;
	protected double onBoardClock;
	protected double pitchKickTime;
	protected double gravTurnTime;
	boolean clampsReleased = false;
	boolean beforeSep = true;
  boolean landed = false;
	private Mission mission = null;

	public TwoStageRocket() {
		mStage = new Stage[2];
		mStage[0] = new Stage("BoosterStage");
		mStage[1] = new Stage("SecondStage");
		mStage[0].setParent(this);
		mStage[1].setParent(this);
	}
	
	@Override
	public void setPayload(Payload pl) {
		payload = pl;
	}
	
	@Override
	public void setClock(double t) {
		onBoardClock = t;
	}
	
	@Override
	public void setMission(Mission m) {
		mission = m;
	}
	
	@Override
	public Mission getMission() {
		return mission;
	}
	
	@Override
	public double clock() {
    return onBoardClock;
	}

	protected void setCoordinates(double incl, double lon) {
		mStage[0].setCoordinates(incl, lon);
		mStage[1].setCoordinates(incl, lon);
	}
	
	@Override
	public int completedOrbits() {
		return mStage[1].completedOrbits();
	}

	@Override
	public void leapfrogFirstStep() {
		mStage[0].setAdditionalMass(mStage[1].getMass() + payload.getMass());
		Navigation.leapfrogFirstStep(mStage[0]);
		
		incrementClock();		//TSR/mission clock
	}

	@Override
	public void leapfrogStep() {

		if (mStage[0].landingBurnIsUnderway() && onBoardClock%3.0 < dt) {
			mStage[0].setThrottle(updateLandingThrottle(mStage[0]));
		}
			
		if (beforeSep) {
			mStage[0].setAdditionalMass(mStage[1].getMass() + payload.getMass());
			Navigation.leapfrogStep(mStage[0]);
		} else {
			mStage[0].setAdditionalMass(0.0);
			Navigation.leapfrogStep(mStage[0]);

			mStage[1].setAdditionalMass(payload.getMass());
			Navigation.leapfrogStep(mStage[1]);
		}

		/*
		 *	I gleaned the '55s' value for starting the gravity turn from /u/Wetmelon's KSP video on /r/spacex
		 */
		if (onBoardClock > gravTurnTime) {
			gravityTurn();
		}
		
		incrementClock();		//TSR/mission clock
	}
	
	private void incrementClock() {
		onBoardClock += dt;
	}

	@Override
	public void invoke(Profile p) {
		/*
			Want to make sure any attitude changes keep effect permanently - not just for the moment they are initiated.
			By 'permanently', I of course mean until it's replaced by the next attitude change.
		*/
		Event event;
		if((event = p.getEvent(onBoardClock)) != null) {
			executeEvent(event);
		}
	}

	@Override
	public void gravityTurn() {
		Navigation.gravityTurn(mStage[0]);
		if (!beforeSep) {
			Navigation.gravityTurn(mStage[1]);
		}
	}

	@Override
	public void outputFile(String id) {
    if(!landed)
      mStage[0].outputFile(id);
		if (!beforeSep) {
			mStage[1].outputFile(id);
		}
	}

	@Override
	public void executeEvent(Event e) {

		if (e.getName().equalsIgnoreCase("firstStageIgnition")) {
			mStage[(int)e.getValueOf("stage")].setThrottle(1.0);
			System.out.printf("T%+7.2f\t%.32s\n", e.getTime(), "First Stage Ignition");
		}
		else if (e.getName().equalsIgnoreCase("releaseClamps")) {
			System.out.printf("T%+7.2f\t%.32s\n", e.getTime(), "Release Clamps");
			this.clampsReleased = true;
			this.leapfrogFirstStep();
		}
		else if (e.getName().equalsIgnoreCase("pitchKick")) {
			Navigation.pitchKick(mStage[(int)e.getValueOf("stage")], e.getValueOf("pitch"), e.getValueOf("yaw"));
			System.out.printf("T%+7.2f\t%.32s\n", e.getTime(), "Pitch Kick");
		}
		else if (e.getName().equalsIgnoreCase("MECO1")) {
			mStage[(int)e.getValueOf("stage")].setThrottle(0.0);
			System.out.printf("T%+7.2f\t%.32s\n", e.getTime(), "MECO-1");
		}
		else if (e.getName().equalsIgnoreCase("firstStageSep")) {
			mStage[1].syncWith(mStage[0]);
			beforeSep = false;
			System.out.printf("T%+7.2f\t%.32s\n", e.getTime(), "First Stage Separation");
		}
		else if (e.getName().equalsIgnoreCase("secondStageIgnition")) {
			mStage[(int)e.getValueOf("stage")].setThrottle(1.0);
			mStage[(int)e.getValueOf("stage")].isMoving = true;
			System.out.printf("T%+7.2f\t%.32s\n", e.getTime(), "Second Stage Ignition");
		}
		else if (e.getName().equalsIgnoreCase("boost_start")) {
			mStage[0].setEngines(3);
			mStage[0].setThrottle(1.0);
			mStage[0].setExtraBurnIsUnderway(true);
			System.out.printf("T%+7.2f\t%.32s\n", e.getTime(), "Boostback Start");
		}
		else if (e.getName().equalsIgnoreCase("boost_end")) {
			mStage[0].setThrottle(0.0);
			mStage[0].setExtraBurnIsUnderway(false);
			System.out.printf("T%+7.2f\t%.32s\n", e.getTime(), "Boostback End");
		}
		else if (e.getName().equalsIgnoreCase("entry_start")) {
			mStage[0].setEngines(3);
			mStage[0].setThrottle(1.0);
			mStage[0].setExtraBurnIsUnderway(true);
			System.out.printf("T%+7.2f\t%.32s\n", e.getTime(), "ReEntry Burn Start");
		}
		else if (e.getName().equalsIgnoreCase("entry_end")) {
			mStage[0].setThrottle(0.0);
			mStage[0].setExtraBurnIsUnderway(false);
			System.out.printf("T%+7.2f\t%.32s\n", e.getTime(), "ReEntry Burn End");
		}
		else if (e.getName().equalsIgnoreCase("landing_start")) {
			mStage[0].setEngines(1);
			mStage[0].executeLandingBurn();
			System.out.printf("T%+7.2f\t%.32s\n", e.getTime(), "Landing Burn Start");
		}
		else if (e.getName().equalsIgnoreCase("SECO1")) {
			mStage[(int)e.getValueOf("stage")].setThrottle(0.0);
			System.out.printf("T%+7.2f\t%.32s\n", e.getTime(), "SECO-1");
		}
		else if (e.getName().equalsIgnoreCase("secondStageSep")) {
			System.out.printf("T%+7.2f\t%.32s\n", e.getTime(), "Second Stage Separation");
		}
		
		if(e.getName().startsWith("adjust")) {
			Navigation.adjustPitch(mStage[(int)e.getValueOf("stage")], e.getValueOf("pitch"));
		}
		else if(e.getName().startsWith("thrust")) {
			mStage[(int)e.getValueOf("stage")].setThrottle(e.getValueOf("throttle"));
		}
	}

	@Override
	public boolean reachesOrbitalVelocity() {
		return mStage[1].absVel() > sqrt(gravConstant*massOfEarth/(radiusOfEarth + alt()));
	}

	@Override
	public boolean depletesFuel() {
		return mStage[1].getPropMass() < 100;
	}
	
	@Override
	public void setLegs(boolean legs) {
		mStage[0].setLegs(legs);
	}
  
  @Override
  public void setLanded(boolean b) {
    landed = b;
  }
	
  @Override
  public boolean isLanded() {
    return landed;
  }
  
	@Override
	public double alt() {
		return mStage[1].alt();
	}
	
	@Override
	public void setLaunchSite(LaunchSite ls) {
		setCoordinates(ls.getIncl(), ls.getLong());
	}
}
