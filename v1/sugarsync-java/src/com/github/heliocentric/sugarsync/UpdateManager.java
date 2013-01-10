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
	
	public UpdateManager(StorageEngine DataEngine, ExecutorService ThreadPool) {
		LinkedBlockingQueue TempIn = new LinkedBlockingQueue<Event>();
		LinkedBlockingQueue TempOut = new LinkedBlockingQueue<Event>();
		this.In = TempIn;
		this.Out = TempOut;
		this.DataEngine = DataEngine;
	}
	private Thread thread;
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
					System.out.println(evn.Properties.get("directory") + File.separator + evn.Properties.get("name"));
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

	@Override
	public void setMessagePump(MessagePump Message) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public String getClassID() {
		return "826d9ced-ea7f-4928-9075-7d7cd4852cfb";
	}
	
}
