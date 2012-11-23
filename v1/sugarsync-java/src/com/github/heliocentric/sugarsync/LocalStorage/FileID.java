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
	public StorageObject object;
	@Override
	public String toString() {
		return this.getUUID();
	}
	public String getPath() {
		return this.object.getAttributeString("path");
	}
	public String getUUID() {
		return this.object.getAttributeString("uuid");
	}
	
	public FileID() {
		this.object = new StorageObject();
		
	}
}
