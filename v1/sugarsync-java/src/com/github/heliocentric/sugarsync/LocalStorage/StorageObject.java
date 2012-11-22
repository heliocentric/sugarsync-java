/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.heliocentric.sugarsync.LocalStorage;

/**
 *
 * @author Helio
 */
public class StorageObject {
	private String UUID;
	public String getUUID() {
		return this.UUID;
	}
	public void setUUID(String UUID) {
		this.UUID = UUID;
	}
	
	private String Table;
	public String getTable() {
		return this.Table;
	}
	public void setTable(String table) {
		this.Table = table;
	}
	
	private StorageEngine Engine;
	public StorageEngine getEngine() {
		return this.Engine;
	}
	public void setEngine(StorageEngine engine) {
		this.Engine = engine;
	}
			
	public StorageObject() {
		
	}
	public StorageObject(StorageEngine engine) {
		this.Engine = engine;
	}
	public String getAttributeString(String Name) {
		return Engine.getAttributeString(this, Name);
	}
	public boolean setAttributeString(String Name, String Value) {
		return this.Engine.setAttributeString(this, Name, Value);
	}
	public Integer getAttributeInt(String Name) {
		return Engine.getAttributeInt(this, Name);
	}
	public boolean setAttributeInt(String Name, Integer Value) {
		return this.Engine.setAttributeInt(this, Name, Value);
	}
	public boolean BeginTransaction() throws StorageEngineException {
		return this.Engine.BeginTransaction();
	}
	public boolean CommitTransaction() throws StorageEngineException {
		return this.Engine.CommitTransaction();
	}
	public boolean RollbackTransaction() throws StorageEngineException {
		return this.Engine.RollbackTransaction();
	}
	
}
