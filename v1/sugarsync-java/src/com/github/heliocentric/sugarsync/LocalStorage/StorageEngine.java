/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.heliocentric.sugarsync.LocalStorage;

import java.io.File;

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
	public abstract FileID getFileID(Domain domain, String file) throws StorageEngineException;
	public void AddFolder(Domain domain, String Folder) throws StorageEngineException {
		File root = new File(Folder);
		File[] list = root.listFiles();
		if (list != null) {
			for (File file : list) {
				if (file.isDirectory()) {
					this.AddFolder(domain, file.getAbsolutePath());
				} else if (file.isFile()) {
					this.AddFile(domain, file.getAbsolutePath());
				}
			}
		}
	}
	public void AddFile(Domain domain, String File) throws StorageEngineException {
		System.out.println(this.getFileID(domain, File).toString());
	}

	public void AddDomain(String Folder) throws StorageEngineException {
		try {
			this.BeginTransaction();
			Domain domain = new Domain();
			this.AddFolder(domain, Folder);
			this.CommitTransaction();
		} catch (StorageEngineException e) {
			this.RollbackTransaction();
		}
	}
}
