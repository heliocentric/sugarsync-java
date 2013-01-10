/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.heliocentric.sugarsync;

import com.github.heliocentric.sugarsync.LocalStorage.StorageEngine;
import java.io.File;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author helio
 */
public class UpdateManager implements Agent {
	
	UpdateManager(StorageEngine DataEngine, ExecutorService ThreadPool) {
		this.In = new LinkedBlockingQueue<Event>();
		this.Out = new LinkedBlockingQueue<Event>();
		this.DataEngine = DataEngine;
	}
	@Override
	public void Start(){
		this.AgentID = UUID.randomUUID().toString();
		this.thread = new Thread(this);
		this.thread.start();
	}
	public String GetScratchDirectory(Agent agent, String Domain) {
		return "";
	}
	public MessagePump GetUpdatePump(Agent agent) {
		return new MessagePump(this.In, this.Out);
	}
	private BlockingQueue<Event> In;
	private BlockingQueue<Event> Out;
	public StorageEngine DataEngine;
	private String AgentID;
	private Thread thread;
			
	@Override
	public String getAgentID() {
		return this.AgentID;
	}

	@Override
	public void run() {
		boolean end = false;
		while (end == false) {
			try {
				Event evn = this.In.take();
				if (evn.Type.equals("com.github.heliocentric.synchro.FileUpdate.v1")) {
					
				}
			}
			catch (Exception e) {
				
			}
		}
	}

	@Override
	public void Stop() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public String Status() {
		throw new UnsupportedOperationException("Not supported yet.");
	}
	
}
