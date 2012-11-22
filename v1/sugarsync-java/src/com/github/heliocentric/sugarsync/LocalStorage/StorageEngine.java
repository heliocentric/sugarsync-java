/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.heliocentric.sugarsync.LocalStorage;

/**
 *
 * @author Helio
 */
public abstract class StorageEngine {
	public abstract boolean Open() throws StorageEngineException;
	public abstract boolean Upgrade() throws StorageEngineException;
	public abstract boolean BeginTransaction() throws StorageEngineException;
	public abstract boolean CommitTransaction() throws StorageEngineException;
	public abstract boolean RollbackTransaction() throws StorageEngineException;
	public abstract String GetSchema() throws StorageEngineException;
	protected abstract String AddFolderRec() throws StorageEngineException;
	protected abstract String GetDomainObject(String Folder) throws StorageEngineException;
	public abstract String getAttributeString(StorageObject Object, String Name);
	public abstract boolean setAttributeString(StorageObject Object, String Name, String Value);
	
	public abstract Integer getAttributeInt(StorageObject Object, String Name);
	public abstract boolean setAttributeInt(StorageObject Object, String Name, Integer Value);
	
	public void AddFolder(String Folder) throws StorageEngineException {
		
	}

	public void AddDomain(String Folder) throws StorageEngineException {
		String DomainUUID;
		try {
			this.BeginTransaction();
			this.AddFolder(Folder);
			this.CommitTransaction();
		} catch (StorageEngineException e) {
			this.RollbackTransaction();
		}
	}
}
