/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.heliocentric.sugarsync;

import com.github.heliocentric.sugarsync.LocalStorage.SQLiteEngine;
import com.github.heliocentric.sugarsync.LocalStorage.StorageEngine;
import com.github.heliocentric.sugarsync.LocalStorage.StorageEngineException;
import java.util.logging.Level;
import java.util.logging.Logger;


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
		this.DataEngine = new SQLiteEngine();
		try {
			this.DataEngine.Open();
		} catch (StorageEngineException ex) {
			Logger.getLogger(Synchro.class.getName()).log(Level.SEVERE, null, ex);
		}
		this.DataEngine.AddFolder(this.settings.get("path"));
	}
	public StorageEngine DataEngine;
}
