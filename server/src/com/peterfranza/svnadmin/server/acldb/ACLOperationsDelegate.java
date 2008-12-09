package com.peterfranza.svnadmin.server.acldb;

import java.util.ArrayList;
import java.util.List;

import com.peterfranza.svnadmin.server.acldb.ACLDB.Subscription;
import com.peterfranza.svnadmin.server.acldb.ACLDB.User;
import com.peterfranza.svnadmin.server.acldb.utils.UnixCrypt;

public class ACLOperationsDelegate {

	private static Object lock = new Object();
	private static ACLOperationsDelegate instance = null;
	private ACLOperationsDelegate() {}
	public synchronized static ACLOperationsDelegate getInstance() {
		if(instance == null) {
			instance = new ACLOperationsDelegate();
			instance.reload();
		}
		return instance;
	}
	
	private ACLDBFileDelegate acl;
	
	private void reload() {
		try {
			synchronized(lock) {
			acl = new ACLDBFileDelegate();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean authenticate(String username, String enteredPassword) {
		synchronized(lock) {
			User u = getUser(username);
			if(u != null) {
				return UnixCrypt.matches(u.getPassword(), enteredPassword);
			} 
			return false;
		}
	}
	
	private User getUser(String username) {
			for(User u: acl.getACL().getUsers()) {
				if(u.getUsername().equals(username)) {
					return u;
				}
			}
		return null;
	}
	public String getEmailForUser(String username) {
		synchronized(lock) {
			User u = getUser(username);
			if(u != null) {
				return u.getEmail();
			} 
		}
		return null;
	}
	
	public List<String> getAddressesSubscribedTo(List<String> changes) {
		List<String> addresses = new ArrayList<String>();
		synchronized(lock) {
			for(User u: acl.getACL().getUsers()) {
				if(isSubscribedTo(u, changes)) {
					addresses.add(u.getEmail());
				}
			}
		}
				
		return addresses;
	}
	
	private boolean isSubscribedTo(User u, List<String> changes) {
		for (String change : changes) {
			for(Subscription s: u.getSubscriptions()) {
				if(change.startsWith(s.getPath())) {
					return true;
				}
			}
		}
		return false;
	}
	
	public List<String> getUsernames() {
		List<String> names = new ArrayList<String>();
		synchronized(lock) {
			for(User u: acl.getACL().getUsers()) {
				names.add(u.getUsername());
			}
		}
		return names;
	}
	
	public void setPassword(String username, String newPassword) {
		synchronized(lock) {
			User u = getUser(username);
			if(u != null) {
				u.setPassword(UnixCrypt.crypt(newPassword));
				acl.save();
			}
		}
	}
	public boolean isAdmin(String username) {
		synchronized(lock) {
			User u = getUser(username);
			if(u != null) {
				return u.isAdmin();
			}
		}
		return false;
	}
	
}
