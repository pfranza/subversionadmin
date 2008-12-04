package com.peterfranza.svnadmin.server.htpasswd;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class HtPasswordFileWrapper {

	private HtPasswd passwd;
	private File file;
	
	public HtPasswordFileWrapper(File f) throws Exception {
		passwd = new HtPasswd(new FileInputStream(f));
		this.file = f;
		
	}
	
	public void setUserPassword(String username, String password) {
		passwd.setUserPassword(username, password);
		save();
	}
	
	public void removeUser(String username) {
		passwd.removeUser(username);
		save();
	}
	
	public boolean authenticate(String username, String password) {
		return passwd.authenticate(username, password);
	}
	
	private void save() {
		try {
			passwd.save(new FileOutputStream(file));	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
