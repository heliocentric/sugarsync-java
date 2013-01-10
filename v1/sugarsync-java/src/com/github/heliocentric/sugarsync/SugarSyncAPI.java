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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author helio
 */
public class SugarSyncAPI extends FileProvider {
	public SugarSyncAPI() {
		this.AgentID = UUID.randomUUID().toString();
	}
    private String UserAgent = "SugarSync-Java/1.0";
	private String AUTH_ACCESS_TOKEN_API_URL = "https://api.sugarsync.com/authorization";
	private String ACCESS_TOKEN_AUTH_REQUEST_TEMPLATE = 
			"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
			+ "<tokenAuthRequest>"
			+ "<accessKeyId>%s</accessKeyId>"
			+ "<privateAccessKey>%s</privateAccessKey>"
			+ "<refreshToken>%s</refreshToken>"
			+ "</tokenAuthRequest>";
	private String fillRequestTemplate(String accessKey, String privateAccessKey, String refreshToken) {
        return String.format(this.ACCESS_TOKEN_AUTH_REQUEST_TEMPLATE, new Object[] { accessKey, privateAccessKey,
                refreshToken });
    }
	public void start() {
		String charset = "UTF-8";
		try {
			this.Connection = new URL("http://www.niftyengineering.com").openConnection();
			this.Connection.setRequestProperty("Accept-Charset", charset);
			this.Connection.setRequestProperty("User-Agent", this.UserAgent);
			
			BufferedReader in = new BufferedReader(new InputStreamReader(this.Connection.getInputStream()));
			String readline;
			while ((readline = in.readLine()) != null) {
				System.out.println(readline);
			}
		} catch (IOException ex) {
			Logger.getLogger(SugarSyncAPI.class.getName()).log(Level.SEVERE, null, ex);
		}
		
	}
	
	URLConnection Connection;
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

	private String AgentID;
	@Override
	public String getAgentID() {
		return this.AgentID;
	}
}
