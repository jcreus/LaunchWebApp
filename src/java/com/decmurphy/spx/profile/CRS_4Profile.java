package com.decmurphy.spx.profile;

/**
 *
 * @author dmurphy
 */
public class CRS_4Profile extends Profile {

	private static final CRS_4Profile instance = new CRS_4Profile();

	private CRS_4Profile() {
	}

	public static Profile getProfile() {
		return instance;
	}

	@Override
	public void firstStageIgnition() {
		super.firstStageIgnition();
	}

	@Override
	public void releaseClamps() {
		super.releaseClamps();
	}

	@Override
	public void pitchKick() {
		super.pitchKick();
	}

	@Override
	public void MECO() {
		super.MECO();
	}

	@Override
	public void stageSeparation() {
		super.stageSeparation();
	}

	@Override
	public void secondStageIgnition() {
		super.secondStageIgnition();
	}

	@Override
	public void SECO() {
		super.SECO();
	}

}
