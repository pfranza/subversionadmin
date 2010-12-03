package com.peterfranza.gwt.svnadmin.server.repositorydata.svn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.peterfranza.gwt.svnadmin.server.entitydata.Entity;
import com.peterfranza.gwt.svnadmin.server.entitydata.Group;
import com.peterfranza.gwt.svnadmin.server.entitydata.GroupManager;
import com.peterfranza.gwt.svnadmin.server.entitydata.User;
import com.peterfranza.gwt.svnadmin.server.entitydata.UserManager;
import com.peterfranza.gwt.svnadmin.server.entitydata.cache.CachedUserManager;
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
		this.userManager = new CachedUserManager(userManager, 30000);
		this.groupManager = groupManager;
		this.authorsFileWriter = authorsFileWriter;
		this.reposManager = reposManager;
	}
	
	private String implode(Collection<User> users) {
		StringBuffer buf = new StringBuffer();
		for (Iterator<User> iterator = users.iterator(); iterator.hasNext();) {
			User user = iterator.next();
			if(user != null) {
				buf.append(user.getName().toLowerCase());
				if(iterator.hasNext()) {
					buf.append(", ");
				}
			}
		}
		return buf.toString();
	}
	
	@Override
	public void saveData() {
		StringBuffer buf = new StringBuffer();
		
		buf.append("[groups]").append(System.getProperty("line.separator"));
		for(Group u: groupManager.getGroups()) {
			buf.append(u.getName()).append(" = ").append(implode(getAllUsers(u))).append(System.getProperty("line.separator"));
		}
		
		buf.append(System.getProperty("line.separator"));
		buf.append(System.getProperty("line.separator"));
		
		buf.append("[/]").append(System.getProperty("line.separator"));
		buf.append("* = r").append(System.getProperty("line.separator"));
		for(User u: userManager.getUsers()) {
			if(u.isAdministrator()) {
				buf.append(u.getName().toLowerCase()).append(" = rw").append(System.getProperty("line.separator"));
			}
		}

		for(Project p: reposManager.getProjects()) {
			buf.append(System.getProperty("line.separator"));
			buf.append("["+p.getPath()+"]").append(System.getProperty("line.separator"));
			
			for(Person person: initializePermissions(getNewPersonCollection(userManager), p.getPath(), 
					groupManager, userManager, reposManager).getPeople()) {
				if(person.canWrite) {
					buf.append(person.person.getName()).append(" = rw").append(System.getProperty("line.separator"));
					if(!person.person.getName().equals(person.person.getName().toLowerCase())) {
						buf.append(person.person.getName().toLowerCase()).append(" = rw").append(System.getProperty("line.separator"));
					}
				} else if(person.canRead) {
					buf.append(person.person.getName()).append(" = r").append(System.getProperty("line.separator"));
					if(!person.person.getName().equals(person.person.getName().toLowerCase())) {
						buf.append(person.person.getName().toLowerCase()).append(" = r").append(System.getProperty("line.separator"));
					}
				} else {
					buf.append(person.person.getName()).append(" = ").append(System.getProperty("line.separator"));
					if(!person.person.getName().equals(person.person.getName().toLowerCase())) {
						buf.append(person.person.getName().toLowerCase()).append(" = ").append(System.getProperty("line.separator"));
					}
				}
			}
			
		}
		
		authorsFileWriter.save(buf.toString().trim());
	}
	
	private static PersonCollection initializePermissions(PersonCollection collection,
			String reposPath, GroupManager groupManager, 
			UserManager userManager, RepositoryManager reposManager) {
		for(Group grp: groupManager.getGroups()) {
			if(reposManager.canWrite(reposPath, grp)) {
				for(User u: getAllUsers(grp)) {
					collection.setWrite(u);
				}
			} else if(reposManager.canRead(reposPath, grp)) {
				for(User u: getAllUsers(grp)) {
					collection.setRead(u);	
				}
			} 
		}
		for(User u: userManager.getUsers()) {
			if(reposManager.canWrite(reposPath, u)) {
				collection.setWrite(u);
			} else if(reposManager.canRead(reposPath, u)) {
				collection.setRead(u);
			} 
		}
		return collection;
	}
	
	private static Collection<User> getAllUsers(Group grp) {
		ArrayList<User> p = new ArrayList<User>();
		for(Entity e: grp.getMembers()) {
			if(e instanceof User) {
				p.add((User)e);
			} else if( e instanceof Group) {
				p.addAll(getAllUsers((Group)e));
			}
		}
			
		return p;
	}
	
	
	private static PersonCollection getNewPersonCollection(UserManager userManager) {
		PersonCollection pc = new PersonCollection();
		for(User u: userManager.getUsers()) {
			pc.people.put(u.getName(), new Person(u));
		}
		return pc;
	}
	
	private static class Person implements Comparable<Person>{
		private User person;
		private boolean canRead;
		private boolean canWrite;
		
		private Person(User u) {
			this.person = u;
			if(u.isAdministrator()) {
				canWrite = true;
			}
 		}

		@Override
		public int compareTo(Person o) {
			return person.getName().compareToIgnoreCase(o.person.getName());
		}
		
	}
	
	private static class PersonCollection {
		private HashMap<String, Person> people = new HashMap<String, Person>();

		public void setWrite(User u) {
			people.get(u.getName()).canWrite = true;
		}

		public void setRead(User u) {
			people.get(u.getName()).canRead = true;
		}
		
		public Collection<Person> getPeople() {
			ArrayList<Person> p = new ArrayList<Person>(people.values());
			Collections.sort(p);
			return p;
		}
		
	}
	
}
