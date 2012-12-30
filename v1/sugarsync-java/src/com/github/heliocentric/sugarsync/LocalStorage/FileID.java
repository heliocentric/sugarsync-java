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
	
	public Revision getCurrentRevision() throws StorageEngineException {
            return this.object.getEngine().GetRevision(this.getUUID(),true);
        }
	public void AddRevision(long date, String previous, String sha256, String md5) throws StorageEngineException {
		this.object.BeginTransaction();
		HashList hashlist = new HashList();
		hashlist.object = this.object.getEngine().NewObject("hashlist");
		
		hashlist.setMD5(md5);
		hashlist.setSHA256(sha256);
		
		Revision newrev = new Revision();
		newrev.object = this.object.getEngine().NewObject("revision");
		
		newrev.setCurrentHash(hashlist.getUUID());
		newrev.setPreviousHash(previous);
		newrev.setDate(String.valueOf(date));
		newrev.setFileID(this.getUUID());
		this.object.CommitTransaction();
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
