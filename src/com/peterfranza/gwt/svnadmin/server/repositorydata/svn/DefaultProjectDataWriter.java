package com.peterfranza.gwt.svnadmin.server.repositorydata.svn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.peterfranza.gwt.svnadmin.server.entitydata.Entity;
import com.peterfranza.gwt.svnadmin.server.entitydata.Group;
import com.peterfranza.gwt.svnadmin.server.entitydata.GroupManager;
import com.peterfranza.gwt.svnadmin.server.entitydata.User;
import com.peterfranza.gwt.svnadmin.server.entitydata.UserManager;
import com.peterfranza.gwt.svnadmin.server.repositorydata.Project;
import com.peterfranza.gwt.svnadmin.server.repositorydata.ProjectDataWriter;
import com.peterfranza.gwt.svnadmin.server.repositorydata.RepositoryManager;
import com.peterfranza.gwt.svnadmin.server.util.ConfigFileWriter;

public class DefaultProjectDataWriter implements ProjectDataWriter {

	private ConfigFileWriter authorsFileWriter;
	private RepositoryManager reposManager;
	private UserManager userManager;
	private GroupManager groupManager;
	
	@Inject
	public DefaultProjectDataWriter(UserManager userManager,
			GroupManager groupManager,
			@Named("authorsFile") ConfigFileWriter authorsFileWriter,
			RepositoryManager reposManager) {
		this.userManager = userManager;
		this.groupManager = groupManager;
		this.authorsFileWriter = authorsFileWriter;
		this.reposManager = reposManager;
	}
	
	private String implode(Collection<User> users) {
		StringBuffer buf = new StringBuffer();
		for (Iterator<User> iterator = users.iterator(); iterator.hasNext();) {
			User user = iterator.next();
			buf.append(user.getName());
			if(iterator.hasNext()) {
				buf.append(", ");
			}
		}
		return buf.toString();
	}
	
	private Collection<User> asUsers(Group group) {
		Collection<User> list = new ArrayList<User>();
		for(Entity e: group.getMembers()) {
			if(e instanceof Group) {
				list.addAll(asUsers((Group)e));
			} else {
				list.add((User)e);
			}
		}
		return list;
	}
	
	@Override
	public void saveData() {
		StringBuffer buf = new StringBuffer();
		
		for(Group u: groupManager.getGroups()) {
			buf.append("@").append(u.getName()).append(" = ").append(implode(asUsers(u))).append(System.getProperty("line.separator"));
		}
		
		buf.append(System.getProperty("line.separator"));
		buf.append(System.getProperty("line.separator"));
		
		buf.append("[/]").append(System.getProperty("line.separator"));
		buf.append("* = r").append(System.getProperty("line.separator"));
		for(User u: userManager.getUsers()) {
			if(u.isAdministrator()) {
				buf.append(u.getName()).append(" = rw").append(System.getProperty("line.separator"));
			}
		}

		for(Project p: reposManager.getProjects()) {
			buf.append(System.getProperty("line.separator"));
			buf.append("["+p.getPath()+"]").append(System.getProperty("line.separator"));
			for(Group u: groupManager.getGroups()) {
				if(reposManager.canWrite(p.getPath(), u)) {
					buf.append(u.getName()).append(" = rw").append(System.getProperty("line.separator"));
				} else if(reposManager.canRead(p.getPath(), u)) {
					buf.append(u.getName()).append(" = r").append(System.getProperty("line.separator"));
				} else {
					buf.append(u.getName()).append(" = ").append(System.getProperty("line.separator"));
				}
			}
			for(User u: userManager.getUsers()) {
				if(reposManager.canWrite(p.getPath(), u)) {
					buf.append(u.getName()).append(" = rw").append(System.getProperty("line.separator"));
				} else if(reposManager.canRead(p.getPath(), u)) {
					buf.append(u.getName()).append(" = r").append(System.getProperty("line.separator"));
				} 
			}
			
		}
		
		authorsFileWriter.save(buf.toString().trim());
	}
	
}
