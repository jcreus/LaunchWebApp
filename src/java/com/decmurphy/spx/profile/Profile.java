package com.decmurphy.spx.profile;

import com.decmurphy.spx.event.Event;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author declan
 */
public class Profile {

	private static final Profile instance = new Profile();

	private Profile() {
		this.events = new ArrayList();
	}

  public static Profile getNew() {
		instance.clean();
		return get();
	}
	
	public static Profile get() {
		return instance;
	}
		
	protected List<Event> events;

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
		for (Event e : events) {
			if (t > e.getTime()) {
				if (e.getName().equalsIgnoreCase("correction")) {
					current = e;
				}
			}
		}
		return current;
	}

	public Event getEvent(String name) {
		for (Event e : events) {
			if (e.getName().equalsIgnoreCase(name)) {
				return e;
			}
		}
		return null;
	}

	public Double getEventTime(String name) {
		for (Event e : events) {
			if (e.getName().equalsIgnoreCase(name)) {
				return e.getTime();
			}
		}
		return null;
	}
	
	public void clean() {
		events.clear();
	}

}
