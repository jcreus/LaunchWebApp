package com.decmurphy.spx.util;

/**
 *
 * @author declan
 */
public enum Payload {
	
	FSAT("FalconSAT-2"),
	DEMO("DemoSat"),
	TBZR("Trailblazer"),
	RATS("RatSat"),
	RAZS("RazakSat"),
	F9F1("Falcon9 Flight 1"),
	COT1("COTS-1"),
	COT2("COTS-2"),
	CRS1("CRS-1"),
	CRS2("CRS-2"),
	CASS("CASSIOPE"),
	SES8("SES-8"),
	THM6("Thaicom-6"),
	CRS3("CRS-3"),
	OG21("Orbcomm OG2 Mission 1"),
	AST8("AsiaSat-8"),
	AST6("AsiaSat-6"),
	CRS4("CRS-4"),
	CRS5("CRS-5"),
	DSCR("DSCOVR"),
	EUAB("EutelSat 115W B/ABS 3A"),
	TRK1("TurkmenSat-1");
	
	private final String payloadName;
	
	private Payload(String payloadName) {
		this.payloadName = payloadName;
	}
	
	public String getPayloadName() {
		return payloadName;
	}
	
	public static Payload getPayloadType(String payloadType) {
		try {
			return Payload.valueOf(payloadType);
		} catch (Exception e) {
		}
		return null;
	}
	
}
