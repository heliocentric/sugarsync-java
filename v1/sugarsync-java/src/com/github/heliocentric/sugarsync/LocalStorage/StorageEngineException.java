/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.heliocentric.sugarsync.LocalStorage;

/**
 *
 * @author Helio
 */
public class StorageEngineException extends Exception {
	private String Type;
	public StorageEngineException() {
		this.Type = "com.github.heliocentric.sugarsync.LocalStorage.UnknownException";
	}
	public StorageEngineException(String Type) {
		this.Type = Type;
	}
	public String getType() {
		return this.Type;
	}
}
