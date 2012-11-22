/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.heliocentric.sugarsync.LocalStorage;

/**
 *
 * @author helio
 */
public class FileID {
	private StorageObject object;
	private String UUID;
	public boolean setUUID(String uuid) {
		boolean retval = true;
		this.UUID = uuid;
		return retval;
	}
	public String getUUID() {
		return this.UUID;
	}
	@Override
	public String toString() {
		return this.UUID;
	}
}
