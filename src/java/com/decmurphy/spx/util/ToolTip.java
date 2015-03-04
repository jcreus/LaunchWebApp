package com.decmurphy.spx.util;

/**
 *
 * @author declan
 */
public enum ToolTip {

	IG1("Time (in seconds) of first stage ignition. Should be negative!"),
	LCH("Liftoff time! Should be 0.0"),
	PTK("Pitch Kick time"),
	PKp("'Pitch' parameter for pitch-kick"),
	PKy("'Yaw' parameter for pitch-kick"),
	MC1("Main Engine CutOff-1 time"),
	SS1("First Stage Separation Time (ideally after MECO1/before Second Stage Ignition)"),
	IG2("Second Stage Ignition time"),
	BBS("Boostback burn start time"),
	BBE("Boostback burn end time"),
	RBS("Re-entry burn start time"),
	RBE("Re-entry burn end time"),
	LBS("Landing burn start time. (Note: burn may not start at exactly this time - but from this point onwards, the sim will continuously calculate optimal thrust for hoverslam. Increases simulation run-time significantly!!!)"),
	PLM("Mass of payload"),
	LGS("Does this flight have legs? Important since this adds mass to the system."),
	STG("Which stage to apply the correction."),
	TYP("What flight parameter do you want to change?"),
	TME("Time (in seconds) after liftoff to begin invoking this correction"),
	PRM("For pitch/yaw, this parameter is in radians. Pitch 0.0 flies parallel to ground, positive points up, etc. For yaw, a positive value turns you left, negative turns you right. (It's safe enough to assume the 2nd stage is always flying prograde). For thrust, enter a % value.")
	;
	
	private final String tip;
	
	private ToolTip(String tip) {
		this.tip = tip;
	}
	
	public String getTip() {
		return tip;
	}
	
}
