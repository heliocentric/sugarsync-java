/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.heliocentric.sugarsync;

/**
 *
 * @author helio
 */
public class SugarSyncAPI {

	public void start() {
	}

	public SugarSyncAPI(String Username, String Password, String AppID, String KeyID, String PrivateKey) {
		this.Username = Username;
		this.Password = Password;
		this.AppID = AppID;
		this.KeyID = KeyID;
		this.PrivateKey = PrivateKey;
	}
	private String AppID;
	private String KeyID;
	private String PrivateKey;
	private String Username;
	private String Password;
}
