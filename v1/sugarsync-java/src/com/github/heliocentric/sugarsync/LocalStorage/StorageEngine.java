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
				PropertyList prop = this.HashAndCopy(File, "");
				sha256_hash = prop.get("sha256");
				md5_hash =  prop.get("md5");
				file_element.AddRevision(last_modified, previous_hash, sha256_hash, md5_hash);
			}
		} catch (Throwable th) {
		}

	}

	public void AddDomain(String Folder) throws StorageEngineException {

			Domain domain = new Domain();
			this.AddFolder(domain, Folder);
	}
	private PropertyList HashAndCopy(String sourcepath, String destinationpath) throws Throwable {
		MessageDigest md5 = MessageDigest.getInstance("md5");
		MessageDigest sha256 = MessageDigest.getInstance("sha-256");
		FileInputStream SourceFile = new FileInputStream(sourcepath);
		PropertyList props = new PropertyList();
		try {
			boolean end = false;
			int counter = 0;
			long previousbpns = 0;
			int BUFFER_SIZE = StorageEngine.INITIAL_BUFFER_SIZE;
			long totalbpns;
			while (end != true) {
				totalbpns = 0;
				byte [] buffer = new byte[BUFFER_SIZE];
				while (2 != 1) {
					long starttime = System.nanoTime();
					if (SourceFile.read(buffer) != -1) {
						md5.update(buffer);
						sha256.update(buffer);
						long endtime = System.nanoTime();
						long duration = (endtime - starttime);
						totalbpns = totalbpns + duration;
						counter += 1;
						if (counter >= 10) {
							long currentbpns = (counter * BUFFER_SIZE) / totalbpns;
							long difference = currentbpns - previousbpns;
							System.out.println("BUFSIZE = " + BUFFER_SIZE + " = Average: " + currentbpns + " Previous: " + previousbpns + " difference: " + difference);
							if (difference > 100) {
								BUFFER_SIZE = BUFFER_SIZE * 2;
							}
							if (difference < -100) {
								BUFFER_SIZE = BUFFER_SIZE / 2;
							}
							if (BUFFER_SIZE < 128) {
								BUFFER_SIZE = 128;
							}
							previousbpns = currentbpns;
							break;
						}
					} else {
						end = true;
						break;
					}
				}
			}
			SourceFile.close();
		} finally {
			SourceFile.close();
		}
		props.put("md5",StorageEngine.DigestToString(md5));
		props.put("sha256",StorageEngine.DigestToString(sha256));
		props.put("copied", "false");
		return props;
	}
	private static String DigestToString(MessageDigest digest) {
		byte[] digestbyte = digest.digest();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < digestbyte.length; i++) {
			sb.append(String.format("%x", digestbyte[i]));
		}
		return sb.toString();
	}
	private static final int INITIAL_BUFFER_SIZE = 512;
	public StorageObject NewObject(String main_table) {
		return this.InsertNewObject(main_table);
	}
	public abstract StorageObject InsertNewObject(String main_table);
}
