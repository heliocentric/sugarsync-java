/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.heliocentric.sugarsync.LocalStorage;

/**
 *
 * @author Helio
 */
public class Revision {
	public StorageObject object;
	public Revision() {
		
	}
	public Revision(StorageObject object) {
		this.object = object;
	}
	
	public String getFileID() {
		return this.object.getAttributeString("fileid");
	}
	public boolean setFileID(String var) {
		return this.object.setAttributeString("fileid", var);
	}
	
	public String getCurrentHash() {
		return this.object.getAttributeString("current_hash");
	}
	public boolean setCurrentHash(String var) {
		return this.object.setAttributeString("current_hash", var);
	}
	
	
	public String getPreviousHash() {
		return this.object.getAttributeString("previous_hash");
	}
	public boolean setPreviousHash(String var) {
		return this.object.setAttributeString("previous_hash", var);
	}
	
	public String getDate() {
		return this.object.getAttributeString("date");
	}
	public boolean setDate(String var) {
		return this.object.setAttributeString("date", var);
	}
	
	public String getUUID() {
		return this.object.getAttributeString("uuid");
	}
}
