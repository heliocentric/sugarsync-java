/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.heliocentric.sugarsync.LocalStorage;

/**
 *
 * @author Helio
 */
public interface StorageEngine {
	public boolean Open() throws StorageEngineException;
	public boolean Upgrade() throws StorageEngineException;
}
