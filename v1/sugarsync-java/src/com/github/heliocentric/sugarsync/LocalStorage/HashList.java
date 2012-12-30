/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.heliocentric.sugarsync.LocalStorage;

/**
 *
 * @author helio
 */
public class HashList {
	public StorageObject object;
	public HashList() {
	}
	public HashList(StorageObject object) {
		this.object = object;
	}
	public String getUUID() {
		return this.object.getAttributeString("uuid");
	}
	public String getSHA256() {
		return this.object.getAttributeString("sha256");
	}
	public boolean setSHA256(String var) {
		return this.object.setAttributeString("sha256", var);
	}
	
	public String getMD5() {
		return this.object.getAttributeString("md5");
	}
	public boolean setMD5(String var) {
		return this.object.setAttributeString("md5", var);
	}
}
