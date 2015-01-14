package com.decmurphy.spx.profile;

import com.decmurphy.spx.event.Event;
import java.util.List;

/**
 *
 * @author declan
 */
public abstract class Profile {
	
	protected List<Event> events;

	protected Profile() {}
	
	public List<Event> events() {
		return events;
	}
	
	public Event addEvent(String name, double time) {
		Event e = new Event(name, time);
		events.add(e);
		return e;
	}
	
	public Event getEvent(double t) {
		Event current = null;
		for(Event e : events) {
			if(e.map().containsKey("pitch") && t > e.getTime())
				current = e;
		}
		return current;
	}
	
	public Event getEvent(String name) {
		for(Event e : events) {
			if(e.getName().equalsIgnoreCase(name)) {
				return e;
			}
		}
		return null;
	}
	
	public Double getEventTime(String name) {
		for(Event e : events) {
			if(e.getName().equalsIgnoreCase(name))
				return e.getTime();
		}
		return null;
	}

}
