/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.heliocentric.sugarsync.LocalStorage;

import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;
import com.github.heliocentric.sugarsync.LocalStorage.StorageEngineException;
import com.github.heliocentric.sugarsync.PropertyList;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Helio
 */
public class SQLiteEngine extends StorageEngine {

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
			if (this.GetSchema().equals("1.1.1")) {
				this.DB.exec("CREATE TABLE fileid (uuid VARCHAR(36) PRIMARY KEY ASC)");
				this.DB.exec("CREATE TABLE hashlist (uuid VARCHAR(36) PRIMARY KEY ASC, sha256 VARCHAR(64), md5 VARCHAR(32))");
				this.DB.exec("UPDATE config SET value='1.1.2' WHERE name='schema'");
			}

			if (this.GetSchema().equals("1.1.2")) {

				this.DB.exec("ALTER TABLE hashlist ADD COLUMN hashtype VARCHAR(255)");
				this.DB.exec("UPDATE config SET value='1.1.3' WHERE name='schema'");
			}

			if (this.GetSchema().equals("1.1.3")) {

				this.DB.exec("CREATE TABLE file_revision (uuid VARCHAR(36) PRIMARY KEY ASC, fileid VARCHAR(36), current_hash VARCHAR(36), previous_hash VARCHAR(36), date TIMESTAMP)");
				this.DB.exec("UPDATE config SET value='1.1.4' WHERE name='schema'");
			}

			if (this.GetSchema().equals("1.1.4")) {
				this.DB.exec("CREATE TABLE filelist (uuid VARCHAR(36) PRIMARY KEY ASC, domain VARCHAR(36), filename VARCHAR(255), fileid VARCHAR(36))");
				this.DB.exec("UPDATE config SET value='1.1.5' WHERE name='schema'");

			}

			if (this.GetSchema().equals("1.1.5")) {
				this.DB.exec("CREATE TABLE domain (uuid VARCHAR(36) PRIMARY KEY ASC, localpath VARCHAR(255))");
				this.DB.exec("UPDATE config SET value='1.1.6' WHERE name='schema'");

			}

			if (this.GetSchema().equals("1.1.6")) {
				this.DB.exec("ALTER TABLE fileid ADD COLUMN domain VARCHAR(36)");
				this.DB.exec("ALTER TABLE fileid ADD COLUMN path VARCHAR(255)");
				this.DB.exec("UPDATE config SET value='1.1.7' WHERE name='schema'");
			}

			if (this.GetSchema().equals("1.1.7")) {
				this.DB.exec("CREATE TABLE account (uuid VARCHAR(36) PRIMARY KEY ASC, type VARCHAR(36), username VARCHAR(255), password VARCHAR(255))");
				this.DB.exec("UPDATE config SET value='1.1.8' WHERE name='schema'");
			}

			if (this.GetSchema().equals("1.1.8")) {
				this.DB.exec("ALTER TABLE file_revision RENAME TO revision");
				this.DB.exec("UPDATE config SET value='1.1.9' WHERE name='schema'");
			}

			if (this.GetSchema().equals("1.1.9")) {
				this.DB.exec("UPDATE config SET value='1.1.10' WHERE name='schema'");
			}

			if (this.GetSchema().equals("1.1.10")) {
				this.DB.exec("CREATE INDEX indx_revision_fileid ON revision (fileid)");
				this.DB.exec("UPDATE config SET value='1.1.11' WHERE name='schema'");
			}

			if (this.GetSchema().equals("1.1.11")) {
				this.DB.exec("INSERT INTO hashlist (uuid,sha256,md5) VALUES('" + StorageEngine.EmptyHash + "','e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855','d41d8cd98f00b204e9800998ecf8427e')");
				this.DB.exec("UPDATE config SET value='1.1.12' WHERE name='schema'");
			}
			if (this.GetSchema().equals("1.1.12")) {
				this.DB.exec("CREATE TABLE _x_object (uuid VARCHAR(36) PRIMARY KEY ASC, link VARCHAR(36), origin VARCHAR(36), date_allocated TIMESTAMP, date_used TIMESTAMP, date_disused TIMESTAMP)");
				int count = 0;
				while (count <= 2000) {
					String query = "INSERT INTO _x_object (uuid,origin,date_allocated) VALUES('" + UUID.randomUUID().toString() + "','" + this.GetHostID() + "','" + System.currentTimeMillis() + "')";
					this.DB.exec(query);
					count = count + 1;
				}
				this.DB.exec("INSERT INTO config (name,value) VALUES('hostid', '" + UUID.randomUUID().toString() + "')");
				this.DB.exec("UPDATE config SET value='1.1.13' WHERE name='schema'");

			}

			this.CommitTransaction();
		} catch (SQLiteException ex) {
			Logger.getLogger(SQLiteEngine.class.getName()).log(Level.SEVERE, null, ex);
		} catch (StorageEngineException e) {
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
		} catch (SQLiteException e) {
			throw new StorageEngineException();
		}
		return "0";
	}
	
	public String GetHostID() throws StorageEngineException {
		try {
			SQLiteStatement st = this.DB.prepare("SELECT value FROM config WHERE name = 'hostid'");
			while (st.step()) {
				return st.columnString(0);
			}
		} catch (SQLiteException e) {
			throw new StorageEngineException();
		}
		return "0";
	}

	@Override
	protected String GetDomainObject(String Folder) throws StorageEngineException {
		String retval = "";
		return retval;
	}

	@Override
	public String getAttributeString(StorageObject Object, String Name) {
		return (String) this.getAttribute(Object, Name, "String");
	}

	@Override
	public boolean setAttributeString(StorageObject Object, String Name, String Value) {
		boolean retval;
		try {
			String query = "UPDATE " + Object.getTable() + " SET " + Name + "='" + Value + "' WHERE uuid='" + Object.getUUID() + "'";
			this.DB.exec(query);
			retval = true;
		} catch (SQLiteException ex) {
			Logger.getLogger(SQLiteEngine.class.getName()).log(Level.SEVERE, null, ex);
			retval = false;
		}
		return retval;
	}

	@Override
	public Integer getAttributeInt(StorageObject Object, String Name) {
		return (Integer) this.getAttribute(Object, Name, "Integer");
	}

	@Override
	public boolean setAttributeInt(StorageObject Object, String Name, Integer Value) {
		return this.setAttributeString(Object, Name, Value.toString());
	}

	private Object getAttribute(StorageObject Object, String Name, String Type) {
		SQLiteStatement st;
		try {
			st = this.DB.prepare("SELECT " + Name + " FROM " + Object.getTable() + " WHERE uuid = '" + Object.getUUID() + "'");
			while (st.step()) {
				if (Type.equals("String")) {
					return st.columnString(0);
				} else if (Type.equals("Integer")) {
					return st.columnInt(0);
				}
			}
		} catch (SQLiteException ex) {
			Logger.getLogger(SQLiteEngine.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	@Override
	protected String AddFolderRec() throws StorageEngineException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public FileID getFileID(Domain domain, String file) throws StorageEngineException {
		FileID fileid = new FileID();
		PropertyList prop = new PropertyList();
		prop.put("domain", domain.object.getUUID());
		prop.put("path", file);
		try {
			fileid.object = this.getExactRecord("fileid", prop, null, 1);
		} catch (StorageEngineException e) {
			if (e.getType().equals("com.github.heliocentric.sugarsync.LocalStorage.RecordNotFound")) {
				fileid.object = this.getNewRecord("fileid");
				fileid.setPath(file);
				fileid.setDomain(domain.object.getUUID());
			}
		}
		return fileid;
	}

	@Override
	public Revision GetRevision(String fileid, Boolean MostRecent) throws StorageEngineException {
		Revision rev = new Revision();
		PropertyList prop = new PropertyList();
		List<String> order = new LinkedList<String>();
		order.add("date");
		prop.put("fileid", fileid);
		try {
			rev.object = this.getExactRecord("revision", prop, order, 1);
		} catch (StorageEngineException e) {
			throw e;
		}
		return rev;
	}

	@Override
	public HashList getHash(String SHA256, String MD5) throws StorageEngineException {
		HashList hash = new HashList();
		PropertyList prop = new PropertyList();
		prop.put("sha256", SHA256);
		prop.put("md5", MD5);
		try {
			hash.object = this.getExactRecord("hashlist", prop, null, 1);
		} catch (StorageEngineException e) {
			throw e;
		}
		return hash;
	}

	public StorageObject getExactRecord(String table, PropertyList Matches, List<String> OrderBy, Integer Limit) throws StorageEngineException {
		StorageObject object = new StorageObject();
		object.setEngine(this);
		object.setTable(table);
		String retuuid;
		SQLiteStatement st;
		try {
			String query;
			query = "SELECT uuid FROM " + table;
			boolean firstwhere = true;
			for (String s : Matches.Map.keySet()) {
				String val = Matches.get(s);
				if (val != null) {
					if (firstwhere == true) {
						query = query + " WHERE";
					} else {
						query = query + " AND";
					}
					query = query + " " + s + " LIKE '" + val + "'";
					firstwhere = false;
				}
			}
			if (OrderBy != null) {
				boolean firstorder = true;
				for (String s : OrderBy) {
					if (s != null) {
						if (firstorder == true) {
							query = query + " ORDER BY";
						} else {
							query = query + ", ";
						}
						query = query + " " + s;
						firstorder = false;
					}
				}
			}
			query = query + " LIMIT " + Limit;
			System.out.println(query);
			st = this.DB.prepare(query);
			retuuid = "";
			while (st.step()) {
				retuuid = st.columnString(0);
			}
			System.out.println(retuuid);
			if (retuuid.equals("")) {
				throw new StorageEngineException("com.github.heliocentric.sugarsync.LocalStorage.RecordNotFound");
			}
		} catch (SQLiteException ex) {
			Logger.getLogger(SQLiteEngine.class.getName()).log(Level.SEVERE, null, ex);
			st = null;
			throw new StorageEngineException();
		}
		return object;
	}

	public StorageObject getNewRecord(String table) {
		StorageObject object = new StorageObject();
		try {
			object.setEngine(this);
			object.setTable(table);
			object.setUUID(UUID.randomUUID().toString());
			String query = "INSERT INTO " + table + " (uuid) VALUES('" + object.getUUID() + "')";
			this.DB.exec(query);
			object.SetNew();
		} catch (SQLiteException ex) {
			Logger.getLogger(SQLiteEngine.class.getName()).log(Level.SEVERE, null, ex);
		}
		return object;
	}

	@Override
	public StorageObject InsertNewObject(String main_table ) {
		StorageObject object = new StorageObject();
		
		return object;
	}
}
