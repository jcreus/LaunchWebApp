package com.decmurphy.spx.event;

/**
 *
 * @author dmurphy
 */
public class Event {
	
	private final double executionTime;
	private final String name;
	
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
}
