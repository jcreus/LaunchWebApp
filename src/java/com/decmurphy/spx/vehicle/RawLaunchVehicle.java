package com.decmurphy.spx.vehicle;

import com.decmurphy.spx.event.Event;
import com.decmurphy.spx.gnc.Navigation;
import com.decmurphy.spx.launchsite.RawLaunchSite;
import com.decmurphy.spx.mission.Mission;
import com.decmurphy.spx.payload.RawPayload;
import com.decmurphy.spx.profile.Profile;
import com.decmurphy.spx.util.Correction;
import com.decmurphy.spx.util.LaunchVehicle;
import static com.decmurphy.utils.Globals.dt;
import static com.decmurphy.utils.Globals.gravConstant;
import static com.decmurphy.utils.Globals.massOfEarth;
import static com.decmurphy.utils.Globals.radiusOfEarth;
import static java.lang.Math.sqrt;

/**
 *
 * @author dmurphy
 */
public abstract class RawLaunchVehicle {
  
  public abstract void leapfrogFirstStep();
  public abstract void leapfrogStep();

  public int numStages;
  public Stage[] mStage;
  protected RawPayload payload;
  private Mission mission = null;
	private LaunchVehicle lv;

  protected double onBoardClock;
  protected double pitchKickTime;
  protected double gravTurnTime;
  boolean clampsReleased = false;
	
	public void setLaunchVehicleType(LaunchVehicle lv) {
		this.lv = lv;
	}
	
	public LaunchVehicle getLaunchVehicleType() {
		return lv;
	}

  public void setPayload(RawPayload pl) {
    payload = pl;
  }

  public void setClock(double t) {
    onBoardClock = t;
  }

  public void setMission(Mission m) {
    mission = m;
  }

  public Mission getMission() {
    return mission;
  }

  public double clock() {
    return onBoardClock;
  }

  public void setCoordinates(double incl, double lon) {
    for (Stage s : mStage) {
      s.setCoordinates(incl, lon);
    }
  }

  public int completedOrbits() {
    return mStage[numStages - 1].completedOrbits(mission.isGoingToPolarOrbit());
  }

  public void incrementClock() {
    onBoardClock += dt;
  }

  public void invoke(Profile p) {
    /*
     Want to make sure any course corrections keep effect permanently - not just 
     for the moment they are initiated. By 'permanently', I of course mean until
     it's replaced by the next correction of the same type.
     */
    Event event;
		for(Correction c : Correction.values()) {
			if ((event = p.getEvent(c, onBoardClock)) != null) {
				executeEvent(event);
			}
		}
  }

  public void gravityTurn() {
    Navigation.gravityTurn(mStage[0]);
    for (int i = 0; i < numStages; i++) {
      if (!mStage[i].beforeSep) {
        Navigation.gravityTurn(mStage[i + 1]);
      }
    }
  }

  public void outputFile() {
    for (int i = 0; i < numStages; i++) {
      if (!mStage[i].landed) {
        if(i==0 || !mStage[i-1].beforeSep)
          mStage[i].outputFile(mission.getMissionId(), false);
      }
    }
  }

	public void executeEvent(Event e) {

		switch(e.getName().toLowerCase()) {
			case "correction":
				Navigation.applyCorrection(
						mStage[(int)e.getValueOf("stage")],
						e.getCorrection("type"),
						e.getValueOf("param")
				);
				break;
			default:
				break;
		}
	}
  
	public boolean reachesOrbitalVelocity() {
		return mStage[numStages-1].absVel() > sqrt(gravConstant*massOfEarth/(radiusOfEarth + alt()));
	}

	public boolean depletesFuel() {
		return mStage[numStages-1].getPropMass() < mStage[numStages-1].getMinimumFuelLimit();
	}
  
  public boolean failedToReachOrbit() {
    return mStage[numStages-1].isLanded();
  }
  
	public void setLegs(boolean legs) {
		mStage[0].setLegs(legs);
	}
  
  public boolean isLanded() {
    for(int i=0;i<numStages-1;i++)
      if(!mStage[i].isLanded()) return false;   // only true if all stages have landed
    return true;                                // (except final stage which ends up in orbit)
  }
  
	public double alt() {
		return mStage[numStages-1].alt();
	}
	
	public void setLaunchSite(RawLaunchSite ls) {
		setCoordinates(ls.getIncl(), ls.getLong());
	}
  
  public boolean hasLaunched() {
    return clampsReleased;
  }

}
