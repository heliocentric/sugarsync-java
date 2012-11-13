/*
 * 
 * Copyright (c) 2012, Dylan Cochran <heliocentric@gmail.com>
 * 
 * Permission to use, copy, modify, and/or distribute this software for any purpose
 * with or without fee is hereby granted, provided that the above copyright 
 * notice and this permission notice appear in all copies.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH
 * REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY 
 * AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, 
 * INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM 
 * LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR
 * OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR 
 * PERFORMANCE OF THIS SOFTWARE.
 * 
 * 
 */
package com.github.heliocentric.sugarsync;

/**
 *
 * @author helio
 */
public class SugarSyncAPI {
	
	private static final String AUTH_ACCESS_TOKEN_API_URL = "https://api.sugarsync.com/authorization";

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
