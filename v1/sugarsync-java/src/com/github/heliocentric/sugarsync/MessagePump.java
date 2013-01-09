/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.heliocentric.sugarsync;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author helio
 */
public class MessagePump {
	public BlockingQueue<Event> In;
	public BlockingQueue<Event> Out;
	public MessagePump() {
		this.In = new LinkedBlockingQueue<Event>();
		this.Out = new LinkedBlockingQueue<Event>();
	}
	public MessagePump(BlockingQueue<Event> in, BlockingQueue<Event> out) {
		this.In = in;
		this.Out = out;
	}
	public Event take() throws InterruptedException {
		return In.take();
	}
	public void put(Event evn) throws InterruptedException {
		Out.put(evn);
	}
}