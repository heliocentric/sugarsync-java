/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.heliocentric.sugarsync;

/**
 *
 * @author helio
 */
public interface Agent extends Runnable {
	public String getAgentID();
	public String getClassID();
	public void Start();
	public void Stop();
	public String Status();
	public void setMessagePump(MessagePump Message);
}
