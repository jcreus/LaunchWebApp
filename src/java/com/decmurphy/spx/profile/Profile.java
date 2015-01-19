package com.decmurphy.spx.profile;

import com.decmurphy.spx.event.Event;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author declan
 */
public abstract class Profile {
	
	protected List<Event> events;

	protected Profile() {
		this.events = new ArrayList();
}
	
	public final List<Event> events() {
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
			if(t > e.getTime()) {
				if(e.getName().startsWith("adjust"))
					current = e;
			}
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
