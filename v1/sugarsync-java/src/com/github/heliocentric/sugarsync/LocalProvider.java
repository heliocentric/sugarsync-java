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
	private MessagePump MessagePump;
	@Override
	public String getAgentID() {
		return this.AgentID;
	}

	@Override
	public void run() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void Start() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void Stop() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public String Status() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void setMessagePump(MessagePump Message) {
		throw new UnsupportedOperationException("Not supported yet.");
	}
	
}
