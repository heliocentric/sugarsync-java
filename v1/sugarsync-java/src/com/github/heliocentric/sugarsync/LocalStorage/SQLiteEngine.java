/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.heliocentric.sugarsync.LocalStorage;

import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Helio
 */
public class SQLiteEngine implements StorageEngine {
	public SQLiteEngine() {
		this.filename = System.getProperty("java.io.tmpdir") + File.separator + "synchro.db";
	}
	public SQLiteEngine(String string) {
		this.filename = string;
	}
	private String filename;
	private SQLiteConnection DB;
	private int TransactionLock;
	@Override
	public boolean Open() throws StorageEngineException {
		boolean retval = false;
		boolean newfile = false;
		this.TransactionLock = 0;
		File DBFile = new File(this.filename);
		if (!DBFile.exists()) {
			newfile = true;
		}
		
		this.DB = new SQLiteConnection(DBFile);
		try {
			this.DB.open(true);
			if (newfile) {
				this.BeginTransaction();
				this.DB.exec("CREATE TABLE config (name VARCHAR(255) PRIMARY KEY ASC, value VARCHAR(255))");
				this.DB.exec("INSERT INTO config (name, value) VALUES ('schema','1.1.1')");
				this.DB.exec("INSERT INTO config (name, value) VALUES ('application','synchro-0.1')");
				this.CommitTransaction();
			}
			retval = true;
		} catch (SQLiteException ex) {
			Logger.getLogger(SQLiteEngine.class.getName()).log(Level.SEVERE, null, ex);
		}
		this.Upgrade();
		return retval;
	}
	
	@Override
	public boolean Upgrade() throws StorageEngineException {
		boolean retval = false;
		try {
			this.BeginTransaction();
			switch (this.GetSchema()) {
				case "1.1.1":
					this.DB.exec("CREATE TABLE fileid (uuid VARCHAR(36) PRIMARY KEY ASC)");
					this.DB.exec("CREATE TABLE hashlist (uuid VARCHAR(36) PRIMARY KEY ASC, sha256 VARCHAR(64), md5 VARCHAR(32))");
					this.DB.exec("UPDATE config SET value='1.1.2' WHERE name='schema'");
				case "1.1.2":
					this.DB.exec("ALTER TABLE hashlist ADD COLUMN hashtype VARCHAR(255)");
					this.DB.exec("UPDATE config SET value='1.1.3' WHERE name='schema'");
				case "1.1.3":
					this.DB.exec("CREATE TABLE file_revision (uuid VARCHAR(36) PRIMARY KEY ASC, fileid VARCHAR(36), current_hash VARCHAR(36), previous_hash VARCHAR(36), date TIMESTAMP)");
					this.DB.exec("UPDATE config SET value='1.1.4' WHERE name='schema'");
			}
			this.CommitTransaction();
		} catch (SQLiteException ex) {
			Logger.getLogger(SQLiteEngine.class.getName()).log(Level.SEVERE, null, ex);
		}
		catch (StorageEngineException e) {
			
		}
		return retval;
	}	
	@Override
	public boolean BeginTransaction() {
		this.TransactionLock += 1;
		if (this.TransactionLock == 1) {
			try {
				this.DB.exec("BEGIN TRANSACTION");
				return true;
			} catch (SQLiteException ex) {
				return false;
			}
		} else {
			return true;
		}
	}

	@Override
	public boolean RollbackTransaction() {
		this.TransactionLock -= 1;
		if (this.TransactionLock <= 0) {
			try {
				this.DB.exec("ROLLBACK");
				return true;
			} catch (SQLiteException ex) {
				Logger.getLogger(SQLiteEngine.class.getName()).log(Level.SEVERE, null, ex);
				return false;
			}
		} else {
			return true;
		}
	}

	@Override
	public boolean CommitTransaction() {
		this.TransactionLock -= 1;
		if (this.TransactionLock <= 0) {
			try {
				this.DB.exec("COMMIT");
				return true;
			} catch (SQLiteException ex) {
				Logger.getLogger(SQLiteEngine.class.getName()).log(Level.SEVERE, null, ex);
				return false;
			} 
		} else {
			return true;
		}
	}

	@Override
	public String GetSchema() throws StorageEngineException {
		try {
		SQLiteStatement st = this.DB.prepare("SELECT value FROM config WHERE name = 'schema'");
		while (st.step()) {
			return st.columnString(0);
		}
		}
		catch (SQLiteException e) {
			throw new StorageEngineException();
		}
		return "0";
	}
	
}
