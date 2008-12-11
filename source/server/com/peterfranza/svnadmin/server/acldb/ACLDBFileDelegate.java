package com.peterfranza.svnadmin.server.acldb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

import com.peterfranza.svnadmin.server.ApplicationProperties;
import com.peterfranza.svnadmin.server.acldb.ACLDB.ACLItem;
import com.peterfranza.svnadmin.server.acldb.ACLDB.AccessRule;
import com.peterfranza.svnadmin.server.acldb.ACLDB.Group;
import com.peterfranza.svnadmin.server.acldb.ACLDB.Subscription;
import com.peterfranza.svnadmin.server.acldb.ACLDB.User;

public class ACLDBFileDelegate {
	
	private ACLDB acl = new ACLDB();
	
	protected ACLDBFileDelegate() throws Exception {
				
		populateUserData();
		String groupsFile = readGroupsFile();
		populateGroups(groupsFile);
		populateSupplementData();		
		populateAccessRules(groupsFile);

	}
	
	protected ACLDB getACL() {
		return acl;
	}

	private void populateSupplementData() throws Exception {
		File f = new File(ApplicationProperties.getProperty("supplement_datafile"));
		if(f.exists()) {
			BufferedReader reader = new BufferedReader(new FileReader(f));

			String line = reader.readLine();
			while(line != null) {
				if(line.trim().length() > 0) {
					String[] parts = line.split(":");
					if(line.startsWith("@")) {
						Group g = getGroup(parts[0].replaceAll("@", ""));
						if(g != null) {
							if(parts.length > 1) {
								for (final String string : parts[1].split("\\s*,\\s*")) {
									g.getSubscriptions().add(new Subscription() {{
										setPath(string);
									}});
								}
							}
						}
					} else {
						User u = getUser(parts[0]);
						if(u != null) {
							u.setEmail(parts[1]);
							u.setAdmin(Boolean.valueOf(parts[2]));
							try {
								String[] subscriptions = parts[3].split("\\s*,\\s*");
								for (final String string : subscriptions) {
									u.getSubscriptions().add(new Subscription() {{
										setPath(string);
									}});
								}
							} catch (ArrayIndexOutOfBoundsException a) {}
						}
					}
				}

				line = reader.readLine();
			}
		}
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

					Group group = acl.createNewGroup();
						group.setName(grp[0]);
						if(grp.length > 1) {
							for (String member : grp[1].split("\\s*,\\s*")) {
								if(member.trim().startsWith("@")) {
									Group m = acl.createNewGroup();
									m.setName(member);
									group.addMember(m);
								} else {
									User u = getUser(member);
									if(u != null) {
										group.getMembers().add(u);
									}
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
		File f = new File(ApplicationProperties.getProperty("svn_groups"));
		if(f.exists()) {
			BufferedReader reader = new BufferedReader(new FileReader(f));
			StringBuffer buf = new StringBuffer();
			String line = reader.readLine();
			while(line != null) {
				if(!line.startsWith("#") && line.trim().length() > 0) {
					buf.append(line.trim()).append(System.getProperty("line.separator"));
				}
				line = reader.readLine();
			}
			return buf.toString().trim();
		} else {
			throw new RuntimeException("Groups File Not Found");
		}
	}

	private void populateUserData()
			throws FileNotFoundException, IOException {
		File f = new File(ApplicationProperties.getProperty("access_file"));
		if(f.exists()) {
			BufferedReader reader = new BufferedReader(new FileReader(f));
			String line = reader.readLine();
			while(line != null) {
				String[] parts = line.split(":");
				acl.getUsers().add(acl.createNewUser(parts[0], parts[1]));
				line = reader.readLine();
			}
		} else {
			throw new RuntimeException("Htpasswd File Not Found");
		}
	}
	
	public synchronized void save() {
		String buf = savePasswordFile();
		String group = saveGroupsFile();
		String supplement = saveSupplementData();
		
		System.out.println(buf);
		System.out.println("------------------------");
		System.out.println(group);
		System.out.println("------------------------");
		System.out.println(supplement);
		
		writeFile(buf, ApplicationProperties.getProperty("access_file"));
		writeFile(group, ApplicationProperties.getProperty("svn_groups"));
		writeFile(supplement, ApplicationProperties.getProperty("supplement_datafile"));
	}

	private void writeFile(String buf, String fileName) {
		try {
			FileWriter writer = new FileWriter(new File(fileName));
			writer.write(buf);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	private String saveSupplementData() {
		StringBuffer buf = new StringBuffer();
		
		for(User u: acl.getUsers()) {
			buf.append(u.getUsername()).append(":").append(u.getEmail())
				.append(":").append(Boolean.toString(u.isAdmin()))
				.append(":").append(printSubscriptions(u.getSubscriptions()))
				.append(System.getProperty("line.separator"));
		}
		
		for(Group g: acl.getGroups()) {
			buf.append("@").append(g.getName()).append(":")
				.append(printSubscriptions(g.getSubscriptions()))
				.append(System.getProperty("line.separator"));
		}
		
		return buf.toString();
	}

	private String printSubscriptions(List<Subscription> subscriptions) {
		StringBuffer buf = new StringBuffer();
		for (Iterator<Subscription> iterator = subscriptions.iterator(); iterator.hasNext();) {
			Subscription subscription = iterator.next();
			buf.append(subscription.getPath());
			if(iterator.hasNext()) {
				buf.append(", ");
			}
		}
		return buf.toString().trim();
	}

	private String saveGroupsFile() {
		StringBuffer buf = new StringBuffer();
		if(acl.getGroups().size() > 0) {
			buf.append("[groups]").append(System.getProperty("line.separator"));
			for(Group g: acl.getGroups()) {
				buf.append(g.getName()).append(" = ");
				for (Iterator<ACLItem> iterator = g.getMembers().iterator(); iterator.hasNext();) {
					ACLItem item = iterator.next();
					if(item != null) {
						buf.append(item.toString());
						if(iterator.hasNext()) {
							buf.append(", ");
						}
					}
				}
				buf.append(System.getProperty("line.separator"));
			}

			buf.append(System.getProperty("line.separator"));
		}

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
	
//	public static void main(String[] args) throws Exception {
//		ACLDBFileDelegate f = new ACLDBFileDelegate();
//		f.save();
//	}
	
	
}
