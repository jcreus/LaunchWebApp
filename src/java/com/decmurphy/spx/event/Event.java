package com.decmurphy.spx.event;

import java.util.HashMap;

/**
 *
 * @author dmurphy
 */
public class Event {
	
	private final String name;
	private final double executionTime;
	private HashMap<String, Double> extraInfo;
	
	public Event(String name, double time) {
		this.name = name.toLowerCase();
		this.executionTime = time;
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
	
	public double getValueOf(String s) {
		return extraInfo.get(s.toLowerCase());
	}
	
	public HashMap<String, Double> map() {
		return extraInfo;
	}
}
