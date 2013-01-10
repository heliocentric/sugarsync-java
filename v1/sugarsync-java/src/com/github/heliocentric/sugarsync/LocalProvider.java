/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.heliocentric.sugarsync;

import com.github.heliocentric.sugarsync.LocalStorage.StorageEngine;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author helio
 */
public class LocalProvider extends FileProvider {
	public LocalProvider(StorageEngine DataEngine, ExecutorService ThreadPool) {
		this.AgentID = UUID.randomUUID().toString();
		this.DataEngine = DataEngine;
		this.ThreadPool = ThreadPool;
	}
	private String AgentID;
	private MessagePump MessagePump;
	private ExecutorService ThreadPool;
	private StorageEngine DataEngine;
	
	
	@Override
	public String getAgentID() {
		return this.AgentID;
	}

	@Override
	public void run() {
		Event evn = new Event("com.github.heliocentric.synchro.FileUpdate.v1", this);
		evn.Properties.put("directory","wakka");
		evn.Properties.put("name", "test.txt");
		try {
			this.MessagePump.In.put(evn);
		} catch (InterruptedException ex) {
			Logger.getLogger(LocalProvider.class.getName()).log(Level.SEVERE, null, ex);
		}
	}


	private Thread thread;
	@Override
	public void Start(){
		this.thread = new Thread(this);
		this.thread.start();
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
		this.MessagePump = Message;
	}

	@Override
	public String getClassID() {
		return "f40f6bf5-dedd-4550-9b97-0ab030a13345";
	}
	
}
