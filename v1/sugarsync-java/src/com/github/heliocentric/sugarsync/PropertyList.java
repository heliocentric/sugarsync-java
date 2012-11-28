/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.heliocentric.sugarsync;

import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author Helio
 */
public class PropertyList {
    public final HashMap<String, String> Map;
    public PropertyList() {
        this.Map = new HashMap<String,String>();
        
    }
    public String get(String name) {
        return this.Map.get(name.toLowerCase());
    }
    public void put(String name, String value) {
        this.Map.put(name.toLowerCase(), value);
    }
	public Set<String> keySet() {
		return this.Map.keySet();
	}
    public boolean containsKey(String name) {
       return this.Map.containsKey(name.toLowerCase());
    }
    
}
