/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.heliocentric.sugarsync;

import java.util.UUID;

/**
 *
 * @author helio
 */
public class LocalProvider extends FileProvider {
	public LocalProvider() {
		this.AgentID = UUID.randomUUID().toString();
	}
	private String AgentID;
	@Override
	public String getAgentID() {
		throw new UnsupportedOperationException("Not supported yet.");
	}
	
}
