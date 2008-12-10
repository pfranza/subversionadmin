package com.peterfranza.svnadmin.server.acldb;

import java.util.ArrayList;
import java.util.List;

import com.peterfranza.svnadmin.server.acldb.ACLDB.ACLItem;
import com.peterfranza.svnadmin.server.acldb.ACLDB.Group;
import com.peterfranza.svnadmin.server.acldb.ACLDB.Subscription;
import com.peterfranza.svnadmin.server.acldb.ACLDB.User;
import com.peterfranza.svnadmin.server.acldb.utils.UnixCrypt;

public class ACLOperationsDelegate {

	private static ACLOperationsDelegate instance = null;
	private static Object lock = new Object();
	public synchronized static ACLOperationsDelegate getInstance() {
		if(instance == null) {
			instance = new ACLOperationsDelegate();
			instance.reload();
		}
		return instance;
	}
	private ACLDBFileDelegate acl;
	
	private ACLOperationsDelegate() {}
	
	public boolean authenticate(String username, String enteredPassword) {
		synchronized(lock) {
			User u = getUser(username);
			if(u != null) {
				return UnixCrypt.matches(u.getPassword(), enteredPassword);
			} 
			return false;
		}
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
	
	public String getEmailForUser(String username) {
		synchronized(lock) {
			User u = getUser(username);
			if(u != null) {
				return u.getEmail();
			} 
		}
		return null;
	}
	public User getUser(String username) {
			for(User u: acl.getACL().getUsers()) {
				if(u.getUsername().equals(username)) {
					return u;
				}
			}
		return null;
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
	
	public boolean isAdmin(String username) {
		synchronized(lock) {
			User u = getUser(username);
			if(u != null) {
				return u.isAdmin();
			}
		}
		return false;
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
	
	private void reload() {
		try {
			synchronized(lock) {
			acl = new ACLDBFileDelegate();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void save() {
		acl.save();
		reload();
	}
	public void setPassword(String username, String newPassword) {
		synchronized(lock) {
			User u = getUser(username);
			if(u != null) {
				u.setPassword(UnixCrypt.crypt(newPassword));
				save();
			}
		}
	}

	public void addNewUser(String username, String password,
			String email) {
		synchronized(lock) {
			User u = new User(username, UnixCrypt.crypt(password));
				u.setEmail(email);
			acl.getACL().getUsers().add(u);
			save();
		}
	}

	public List<String> getGroupMembership(String username) {
		List<String> l = new ArrayList<String>();
		synchronized(lock) {
			for(Group g: acl.getACL().getGroups()) {
				if(isMemeberOfGroup(username, g)) {
					l.add(g.getName());
				}
			}
		}
		return l;
	}

	private boolean isMemeberOfGroup(String username, Group g) {
		for(ACLItem i: g.getMembers()) {
			if(i instanceof Group) {
				boolean flag = isMemeberOfGroup(username, (Group)i);
				if(flag) {
					return true;
				}
			} else {
				User u = (User)i;
				if(u.getUsername().equals(username)) {
					return true;
				}
			}
		}
		return false;
	}

	public void updateUser(String username, String email,
			String admin, String password) {
		synchronized(lock) {			
			User u = getUser(username);
			u.setEmail(email);
			u.setAdmin(Boolean.valueOf(admin));
			if(password != null && password.length() > 1) {
				u.setPassword(UnixCrypt.crypt(password));
			}
			save();
		}
	}
	
	public void deleteUser(String username) {
		synchronized(lock) {			
			User u = getUser(username);
			acl.getACL().getUsers().remove(u);
			save();
		}
	}
	
}
