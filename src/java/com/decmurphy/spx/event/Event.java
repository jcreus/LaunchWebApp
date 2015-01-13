package com.decmurphy.spx.event;

import java.util.HashMap;

/**
 *
 * @author dmurphy
 */
public class Event {
	
	private final double executionTime;
	private final String name;
	private HashMap<String, Double> extraInfo;
	
	public Event(String name, double time) {
		this.name = name;
		this.executionTime = time;
	}
	
	public String getName() {
		return name;
	}
	
	public double getTime() {
		return executionTime;
	}
	
	public void addExtraInfo(String s, double val) {
		extraInfo.put(s, val);
	}
	
	public double getValueOf(String s) {
		return extraInfo.get(s);
	}
}
