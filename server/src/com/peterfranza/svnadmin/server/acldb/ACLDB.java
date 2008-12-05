package com.peterfranza.svnadmin.server.acldb;

import java.util.ArrayList;
import java.util.List;

public class ACLDB {

	private List<User> users = new ArrayList<User>();
	private List<Group> groups = new ArrayList<Group>();
	private List<AccessRule> rules = new ArrayList<AccessRule>();
	
	private static class ACLItem {}
	
	private static class User extends ACLItem {
		private String username;
		private String password;
	}
	
	private static class Group extends ACLItem {
		private String name;
		private List<ACLItem> members = new ArrayList<ACLItem>();
	}
	
	private static class AccessRule {
		private String directory;
		private List<ACLItem> allow_read = new ArrayList<ACLItem>();
		private List<ACLItem> allow_write = new ArrayList<ACLItem>();
	}
	
}
