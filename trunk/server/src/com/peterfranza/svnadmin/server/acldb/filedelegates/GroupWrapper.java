package com.peterfranza.svnadmin.server.acldb.filedelegates;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GroupWrapper {

	private Map<String, SVNGroup> map = new HashMap<String, SVNGroup>();
	
	public SVNGroup createGroup(String groupName) {
		SVNGroup g = new SVNGroup();
		g.setName(groupName);
		add(g);
		return g;
	}
	
	public void deleteGroup(String groupName) {
		map.remove(groupName);
	}
	
	private void add(SVNGroup group) {
		map.put(group.getName(), group);
	}
	
	public void load(String group) throws IOException {
		BufferedReader reader = new BufferedReader(new StringReader(group));
		String line = reader.readLine();
		while(line != null) {
			String[] pts = line.split("=");
			SVNGroup grp = createGroup(pts[0].trim());
			for(String name: pts[1].split(",")) {
				name = name.trim();
				if(name.startsWith("@")) {
					grp.getUsers().add(new SVNGroupName(name.trim()));
				} else {
					grp.getUsers().add(new SVNUserName(name.trim()));
				}
			}
			line = reader.readLine();
		}
	}
	
	public String save() {
		StringBuffer buf = new StringBuffer("[groups]" + System.getProperty("line.separator"));
		for(SVNGroup g: map.values()) {
			buf.append(g.getName()).append(" = ");
			Iterator<SVNAccessListMemeber> iter = g.getUsers().iterator();
			while(iter.hasNext()) {
				SVNAccessListMemeber next = iter.next();
				if(next.exists()) {
					buf.append(next.toString());
					if(iter.hasNext()) {
						buf.append(", ");
					}
				}
			}
			buf.append(System.getProperty("line.separator"));
		}
		return buf.toString().trim();
	}
	
	public class SVNGroup {
		private String name;
		private boolean globalRead = true;
		private List<SVNAccessListMemeber> users = new ArrayList<SVNAccessListMemeber>();
		public final String getName() {
			return name;
		}
		public final void setName(String name) {
			this.name = name;
		}
		public final boolean isGlobalRead() {
			return globalRead;
		}
		public final void setGlobalRead(boolean globalRead) {
			this.globalRead = globalRead;
		}
		public final List<SVNAccessListMemeber> getUsers() {
			return users;
		}
		public final void setUsers(List<SVNAccessListMemeber> users) {
			this.users = users;
		}
		
		public List<String> getAllMembersOfGroup() {
			ArrayList<String> member = new ArrayList<String>();
			for(SVNAccessListMemeber alm: getUsers()) {
				if(alm instanceof SVNUserName) {
					member.add(alm.toString());
				} else {
					SVNGroupName grp = (SVNGroupName) alm;
					SVNGroup group = map.get(grp.getName());
					if(group != null) {
						member.addAll(group.getAllMembersOfGroup());
					}
				}
			}
			return member;
		}
		
		
	}
	
	private static class SVNAccessListMemeber {

		public boolean exists() {
			return true;
		}
	}
	
	private static class SVNUserName extends SVNAccessListMemeber {
		private String name;

		public SVNUserName(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
		
		@Override
		public String toString() {
			return name;
		}
	}
	
	private class SVNGroupName extends SVNAccessListMemeber {
		private String name;

		public SVNGroupName(String name) {
			this.name = name.replaceFirst("@", "");
		}
		
		public String getName() {
			return name;
		}
		
		@Override
		public String toString() {
			return "@" + name;
		}
		
		public boolean exists() {
			return map.get(name) != null;
		}
	}
	
	
}
