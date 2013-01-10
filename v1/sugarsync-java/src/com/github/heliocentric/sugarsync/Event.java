/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.heliocentric.sugarsync;

/**
 *
 * @author helio
 */
public class Event {
	public Event() {
		this.Properties = new PropertyList();
	}
	public Event(String type, Agent agent) {
		this.Properties = new PropertyList();
		this.Type = type;
		this.agent = agent;
	}
	public Agent agent;
	public String Type;
	public PropertyList Properties;
	public Object Payload;
}
