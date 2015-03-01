package com.decmurphy.spx.event;

import com.decmurphy.spx.util.Correction;
import java.util.HashMap;

/**
 *
 * @author dmurphy
 */
public class Event {
	
	private final String name;
	private final double executionTime;
	private final HashMap<String, Correction> correction;
	private final HashMap<String, Double> extraInfo;
	
	public Event(String name, double time) {
		this.name = name.toLowerCase();
		this.executionTime = time;
		correction = new HashMap();
		extraInfo = new HashMap();
	}
	
	public String getName() {
		return name;
	}
	
	public double getTime() {
		return executionTime;
	}
	
	public Event addExtraInfo(String paramName, double val) {
		extraInfo.put(paramName.toLowerCase(), val);
		return this;
	}
	
	public Event addCorrectionType(String name, Correction type) {
		correction.put(name.toLowerCase(), type);
		return this;
	}
	
	public double getValueOf(String s) {
		return extraInfo.get(s.toLowerCase());
	}
	
	public Correction getCorrection(String s) {
		return correction.get(s.toLowerCase());
	}
	
	public HashMap<String, Double> map() {
		return extraInfo;
	}
}
