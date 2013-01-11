/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.heliocentric.sugarsync.LocalStorage;

import com.github.heliocentric.sugarsync.PropertyList;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import java.security.DigestInputStream;
import java.security.MessageDigest;

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

	public abstract Revision GetRevision(String fileid, Boolean MostRecent) throws StorageEngineException;

	public abstract HashList getHash(String SHA256, String MD5) throws StorageEngineException;
	public static String EmptyHash = "610de42b-3cf9-4da4-bef2-c6c6405c0a60";



	public StorageObject NewObject(String main_table) {
		return this.InsertNewObject(main_table);
	}
	public abstract StorageObject InsertNewObject(String main_table);
}
