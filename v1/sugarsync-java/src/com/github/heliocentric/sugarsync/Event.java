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
	public Event(String type) {
		this.Properties = new PropertyList();
		this.Type = type;
	}
	public String Type;
	public PropertyList Properties;
	public Object Payload;
}
