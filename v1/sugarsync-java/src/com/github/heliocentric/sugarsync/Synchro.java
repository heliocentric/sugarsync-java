/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.heliocentric.sugarsync;

import com.github.heliocentric.sugarsync.LocalStorage.SQLiteEngine;
import com.github.heliocentric.sugarsync.LocalStorage.StorageEngine;
import com.github.heliocentric.sugarsync.LocalStorage.StorageEngineException;
import java.lang.reflect.Field;
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
	public void start() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {

		System.setProperty("java.library.path",System.getProperty("java.library.path") + ":" + System.getProperty("user.dir"));
		Field fieldSysPath = ClassLoader.class.getDeclaredField( "sys_paths" );
		fieldSysPath.setAccessible( true );
		fieldSysPath.set( null, null );
		System.out.println(System.getProperty("java.library.path"));
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
