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
		return this.getPath() + " = " + this.getUUID();
	}
	public String getPath() {
		return this.object.getAttributeString("path");
	}
	public boolean setPath(String var) {
		return this.object.setAttributeString("path", var);
	}
	public String getDomain() {
		return this.object.getAttributeString("domain");
	}
	public boolean setDomain(String var) {
		return this.object.setAttributeString("domain", var);
	}
	public String getUUID() {
		return this.object.getAttributeString("uuid");
	}
	
	public boolean isNew() {
		return this.object.IsNew();
	}
	
	public FileID() {
		this.object = new StorageObject();
	}
	public FileID(StorageObject object) {
		this.object = object;
	}
}
