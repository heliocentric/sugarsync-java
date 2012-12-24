/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.heliocentric.sugarsync.LocalStorage;

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
		FileID file_element;
		String sha256_hash;
		String md5_hash;
		long previous_date;
		String previous_hash;
		long last_modified = new File(File).lastModified();
		try {
			file_element = this.getFileID(domain, File);
			if (file_element.isNew()) {
				previous_date = 0;
				previous_hash = StorageEngine.EmptyHash;
			} else {
				Revision rev = file_element.getCurrentRevision();
				previous_date = Long.parseLong(rev.getDate());
				previous_hash = rev.getCurrentHash();
			}
			if (last_modified > previous_date) {
				sha256_hash = getDigestString(new FileInputStream(File), "SHA-256");
				md5_hash = getDigestString(new FileInputStream(File), "MD5");
				file_element.AddRevision(last_modified, previous_hash, sha256_hash, md5_hash);
			}
		} catch (Throwable th) {
		}

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
	private static final int BUFFER_SIZE = 2048;

	private static byte[] getDigest(InputStream in, String algorithm) throws Throwable {
		MessageDigest md = MessageDigest.getInstance(algorithm);
		try {
			DigestInputStream dis = new DigestInputStream(in, md);
			byte[] buffer = new byte[BUFFER_SIZE];
			while (dis.read(buffer) != -1) {
			}
			dis.close();
		} finally {
			in.close();
		}
		return md.digest();
	}

	private static String getDigestString(InputStream in, String algorithm) throws Throwable {
		byte[] digest = getDigest(in, algorithm);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < digest.length; i++) {
			sb.append(String.format("%x", digest[i]));
		}
		return sb.toString();
	}
	public StorageObject NewObject(String main_table) {
		return this.InsertNewObject(main_table);
	}
	public abstract StorageObject InsertNewObject(main_table);
}
