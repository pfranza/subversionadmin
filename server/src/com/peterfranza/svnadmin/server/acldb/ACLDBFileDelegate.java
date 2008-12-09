package com.peterfranza.svnadmin.server.acldb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

import com.peterfranza.svnadmin.server.ApplicationProperties;
import com.peterfranza.svnadmin.server.SupplementalData;
import com.peterfranza.svnadmin.server.acldb.ACLDB.ACLItem;
import com.peterfranza.svnadmin.server.acldb.ACLDB.AccessRule;
import com.peterfranza.svnadmin.server.acldb.ACLDB.Group;
import com.peterfranza.svnadmin.server.acldb.ACLDB.User;

public class ACLDBFileDelegate {
	
	private ACLDB acl = new ACLDB();
	
	public ACLDBFileDelegate() throws Exception {
		
		SupplementalData supplement = SupplementalData.getInstance();			
		populateUserData(supplement);
		
		String groupsFile = readGroupsFile();
		populateGroups(groupsFile);
		populateAccessRules(groupsFile);
		
	}

	private void populateAccessRules(String groupsFile) throws IOException {
		BufferedReader reader = new BufferedReader(new StringReader(groupsFile));
		String line = reader.readLine();
		boolean notGroupsSection = true;
		String directory = "";
		while(line != null) {
			line = line.replaceAll("\\s*", "");
			if(line.matches("^\\[.*\\]")) {
				notGroupsSection = !line.matches("^\\[groups\\]");
				if(line.contains(":")) {
					int colon = line.indexOf(":") + 1;
					directory = line.substring(colon, line.indexOf(']', colon));
				}
			} else {
				if(notGroupsSection) {
					
					AccessRule rule = getRule(directory);
					String[] parts = line.split("=");
					List<ACLItem> list = parts[1].equalsIgnoreCase("rw") ? rule.getAllow_write() : rule.getAllow_read();
					if(parts[0].startsWith("@")) {
						Group g = getGroup(parts[0].replace("@", ""));
						if(g != null) {
							list.add(g);
						}
					} else {
						User u = getUser(parts[0]);
						if(u != null) {
							list.add(u);
						}
					}
					
					
				}
			}
			line = reader.readLine();
		}
	}
	
	private AccessRule getRule(String directory) {

		for (AccessRule accessRule : acl.getRules()) {
			if(accessRule.getDirectory().equals(directory)) {
				return accessRule;
			}
		}
		
		AccessRule rule = new AccessRule();
		rule.setDirectory(directory);
		acl.getRules().add(rule);
		return rule;
	}

	private Group getGroup(String name) {
		for(Group g: acl.getGroups()) {
			if(name.equals(g.getName())) {
				return g;
			}
		}
		return null;
	}

	private void populateGroups(String groupsFile) throws IOException {
		BufferedReader reader = new BufferedReader(new StringReader(groupsFile));
		String line = reader.readLine();
		boolean inGroupsSection = false;
		while(line != null) {
			if(line.matches("^\\[.*\\]")) {
				inGroupsSection = line.matches("^\\[groups\\]");
			} else {
				if(inGroupsSection) {
					String[] grp = line.split("\\s*=\\s*");
					String[] members = grp[1].split("\\s*,\\s*");

					Group group = new Group();
						group.setName(grp[0]);
						for (String member : members) {
							if(member.trim().startsWith("@")) {
								Group m = new Group();
								m.setName(member);
								group.getMembers().add(m);
							} else {
								User u = getUser(member);
								if(u != null) {
									group.getMembers().add(u);
								}
							}
						}
					acl.getGroups().add(group);
					
				}
			}
		
			line = reader.readLine();
		}
	}

	private User getUser(String member) {
		for(User u: acl.getUsers()) {
			if(member.equals(u.getUsername())) {
				return u;
			}
		}
		return null;
	}

	private String readGroupsFile() throws FileNotFoundException, IOException {
		BufferedReader reader = new BufferedReader(new FileReader(new File(ApplicationProperties.getProperty("svn_groups"))));
		StringBuffer buf = new StringBuffer();
		String line = reader.readLine();
		while(line != null) {
			if(!line.startsWith("#") && line.trim().length() > 0) {
				buf.append(line.trim()).append(System.getProperty("line.separator"));
			}
			line = reader.readLine();
		}
		return buf.toString().trim();
	}

	private void populateUserData(SupplementalData supplement)
			throws FileNotFoundException, IOException {
		BufferedReader reader = new BufferedReader(new FileReader(new File(ApplicationProperties.getProperty("access_file"))));
		String line = reader.readLine();
		while(line != null) {
			String[] parts = line.split(":");
			acl.getUsers().add(new User(
					parts[0], 
					parts[1], 
					supplement.getUserEmailAddress(parts[0])));
			line = reader.readLine();
		}
	}
	
	public void save() {
		String buf = savePasswordFile();
		String group = saveGroupsFile();
		
		System.out.println(buf);
		System.out.println("------------------------");
		System.out.println(group);
	}

	private String saveGroupsFile() {
		StringBuffer buf = new StringBuffer();
		buf.append("[groups]").append(System.getProperty("line.separator"));
		for(Group g: acl.getGroups()) {
			if(g.getMembers().size() > 0) {
				buf.append(g.getName()).append(" = ");
				List<ACLItem> members = g.getMembers();
				for (Iterator<ACLItem> iterator = members.iterator(); iterator.hasNext();) {
					ACLItem item = iterator.next();
					buf.append(item.toString());
					if(iterator.hasNext()) {
						buf.append(", ");
					}
				}
				buf.append(System.getProperty("line.separator"));
			}
		}
		
		buf.append(System.getProperty("line.separator"));
		for (AccessRule accessRule : acl.getRules()) {
			if(accessRule.getAllow_read().size() + accessRule.getAllow_write().size() > 0) {
				buf.append("[proj:"+accessRule.getDirectory()+"]").append(System.getProperty("line.separator"));
				for(ACLItem i: accessRule.getAllow_write()) {
					buf.append(i.toString()).append(" = rw").append(System.getProperty("line.separator"));
				}
				for(ACLItem i: accessRule.getAllow_read()) {
					buf.append(i.toString()).append(" = r").append(System.getProperty("line.separator"));
				}
				buf.append(System.getProperty("line.separator"));
			}
		}
		
		return buf.toString().trim();
	}

	private String savePasswordFile() {
		StringBuffer buf = new StringBuffer();
		for(User u: acl.getUsers()) {
			buf.append(u.getUsername()).append(":").append(u.getPassword()).append(System.getProperty("line.separator"));
		}
		return buf.toString().trim();
	}
	
	public static void main(String[] args) throws Exception {
		ACLDBFileDelegate f = new ACLDBFileDelegate();
		f.save();
	}
	
	
}
