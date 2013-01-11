/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.heliocentric.sugarsync;

import com.github.heliocentric.sugarsync.LocalStorage.Revision;
import com.github.heliocentric.sugarsync.LocalStorage.StorageEngine;
import com.github.heliocentric.sugarsync.LocalStorage.StorageEngineException;
import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
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
		boolean end = false;
		while (end == false) {
			try {
				this.AddFolder(this.DomainPath, "");
			} catch (StorageEngineException ex) {
				Logger.getLogger(LocalProvider.class.getName()).log(Level.SEVERE, null, ex);
			}
			end = true;
		}
	}
	public String DomainPath;

	public void AddFolder(String domain, String Folder) throws StorageEngineException {
		File root;
		if (Folder.equals("")) {
			root = new File(domain);
		} else {
			root = new File(domain + File.separator + Folder);
		}
		File[] list = root.listFiles();
		if (list != null) {
			for (File file : list) {
				if (file.isDirectory()) {
					this.AddFolder(domain, Folder + File.separator + file.getName());
				} else if (file.isFile()) {
					FileRunner runner = new FileRunner();
					runner.provider = this;
					runner.DomainPath = domain;
					runner.Folder = Folder;
					runner.File = file;
					this.ThreadPool.submit(runner);
				}
			}
		}
	}

	public class FileRunner implements Runnable {

		public LocalProvider provider;
		public String DomainPath;
		public String Folder;
		public File File;

		@Override
		public void run() {
			try {
				this.AddFile(this.DomainPath, this.Folder, this.File);
			} catch (StorageEngineException ex) {
				Logger.getLogger(LocalProvider.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

		public void AddFile(String domain, String Folder, File file) throws StorageEngineException {

			Event evn = new Event("com.github.heliocentric.synchro.FileUpdate.v1", provider);
			evn.Properties.put("directory", Folder);
			evn.Properties.put("name", file.getName());
			try {
				this.HashAndCopy(File.getAbsolutePath(), "");
			} catch (Throwable ex) {
				Logger.getLogger(LocalProvider.class.getName()).log(Level.SEVERE, null, ex);
			}
			try {
				provider.MessagePump.In.put(evn);
			} catch (InterruptedException ex) {
				Logger.getLogger(LocalProvider.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

		public String DigestToString(MessageDigest digest) {
			byte[] digestbyte = digest.digest();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < digestbyte.length; i++) {
				sb.append(String.format("%x", digestbyte[i]));
			}
			return sb.toString();
		}
		public int INITIAL_BUFFER_SIZE = 512;

		private PropertyList HashAndCopy(String sourcepath, String destinationpath) throws Throwable {
			MessageDigest md5 = MessageDigest.getInstance("md5");
			MessageDigest sha256 = MessageDigest.getInstance("sha-256");
			FileInputStream SourceFile = new FileInputStream(sourcepath);
			PropertyList props = new PropertyList();
			try {
				boolean end = false;
				int counter = 0;
				long previousbpns = 0;
				int BUFFER_SIZE = this.INITIAL_BUFFER_SIZE;
				long totalbpns;
				while (end != true) {
					totalbpns = 0;
					byte[] buffer = new byte[BUFFER_SIZE];
					while (2 != 1) {
						long starttime = System.nanoTime();
						if (SourceFile.read(buffer) != -1) {
							md5.update(buffer);
							sha256.update(buffer);
							long endtime = System.nanoTime();
							long duration = (endtime - starttime);
							totalbpns = totalbpns + duration;
							counter += 1;
							if (counter >= 10) {
								long currentbpns = (counter * BUFFER_SIZE) / totalbpns;
								long difference = currentbpns - previousbpns;
								if (difference > 100) {
									BUFFER_SIZE = BUFFER_SIZE * 2;
								}
								if (difference < -100) {
									BUFFER_SIZE = BUFFER_SIZE / 2;
								}
								if (BUFFER_SIZE < 128) {
									BUFFER_SIZE = 128;
								}
								previousbpns = currentbpns;
								break;
							}
						} else {
							end = true;
							break;
						}
					}
				}
				SourceFile.close();
			} finally {
				SourceFile.close();
			}
			props.put("md5", this.DigestToString(md5));
			props.put("sha256", this.DigestToString(sha256));
			props.put("copied", "false");
			return props;
		}
	}
	private Thread thread;

	@Override
	public void Start() {
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