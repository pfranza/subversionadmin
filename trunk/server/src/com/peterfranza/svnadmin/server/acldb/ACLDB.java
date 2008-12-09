package com.peterfranza.svnadmin.server.acldb;

import java.util.ArrayList;
import java.util.List;

public class ACLDB {

	protected ACLDB() {}
	
	private List<User> users = new ArrayList<User>();
	private List<Group> groups = new ArrayList<Group>();
	private List<AccessRule> rules = new ArrayList<AccessRule>();
	
	protected static class ACLItem {}
	
	protected static class User extends ACLItem {
		private String username;
		private String password;
		private String email;
		private List<Subscription> subscriptions = new ArrayList<Subscription>();
		private boolean isAdmin = false;
		
		protected User(String username2, String password2) {
			this.username = username2;
			this.password = password2;
		}
		
		public final String getUsername() {
			return username;
		}
		public final void setUsername(String username) {
			this.username = username;
		}
		public final String getPassword() {
			return password;
		}
		public final void setPassword(String password) {
			this.password = password;
		}
		public final String getEmail() {
			return email;
		}
		public final void setEmail(String email) {
			this.email = email;
		}
		public final List<Subscription> getSubscriptions() {
			return subscriptions;
		}
		public final void setSubscriptions(List<Subscription> subscriptions) {
			this.subscriptions = subscriptions;
		}
		
		@Override
		public String toString() {
			return username;
		}

		public boolean isAdmin() {
			return isAdmin;
		}
		
		public void setAdmin(boolean b) {
			isAdmin = b;
		}
		
	}
	
	protected static class Group extends ACLItem {
		private String name;
		private List<ACLItem> members = new ArrayList<ACLItem>();
		private List<Subscription> subscriptions = new ArrayList<Subscription>();
		
		public final String getName() {
			return name;
		}
		public final void setName(String name) {
			this.name = name;
		}
		public final List<ACLItem> getMembers() {
			return members;
		}
		public final void setMembers(List<ACLItem> members) {
			this.members = members;
		}
		
		public final List<Subscription> getSubscriptions() {
			return subscriptions;
		}
		
		public final void setSubscriptions(List<Subscription> subscriptions) {
			this.subscriptions = subscriptions;
		}
		
		@Override
		public String toString() {
			return "@" + name;
		}
		
	}
	
	protected static class AccessRule {
		private String directory;
		private List<ACLItem> allow_read = new ArrayList<ACLItem>();
		private List<ACLItem> allow_write = new ArrayList<ACLItem>();
		
		public final String getDirectory() {
			return directory;
		}
		public final void setDirectory(String directory) {
			this.directory = directory;
		}
		public final List<ACLItem> getAllow_read() {
			return allow_read;
		}
		public final void setAllow_read(List<ACLItem> allow_read) {
			this.allow_read = allow_read;
		}
		public final List<ACLItem> getAllow_write() {
			return allow_write;
		}
		public final void setAllow_write(List<ACLItem> allow_write) {
			this.allow_write = allow_write;
		}
		
		
	}
	
	protected static class Subscription {
		private String path;

		public final String getPath() {
			return path;
		}

		public final void setPath(String path) {
			this.path = path;
		}
		
	}

	public final List<User> getUsers() {
		return users;
	}

	public final void setUsers(List<User> users) {
		this.users = users;
	}

	public final List<Group> getGroups() {
		return groups;
	}

	public final void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public final List<AccessRule> getRules() {
		return rules;
	}

	public final void setRules(List<AccessRule> rules) {
		this.rules = rules;
	}
	
	
	
}
