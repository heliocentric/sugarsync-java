/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.heliocentric.sugarsync;

import com.github.heliocentric.sugarsync.LocalStorage.StorageEngine;


/**
 *
 * @author Helio
 */
public class Synchro {
	private PropertyList settings;
	public Synchro(PropertyList options) {
		this.settings = options;
	}
	public void start() {
	}
	public StorageEngine DataEngine;
}
