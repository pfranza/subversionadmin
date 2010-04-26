package com.peterfranza.gwt.svnadmin.server;

import java.util.ArrayList;
import java.util.Collection;

import com.peterfranza.gwt.svnadmin.server.entitydata.Entity;
import com.peterfranza.gwt.svnadmin.server.repositorydata.ChangeSet;
import com.peterfranza.gwt.svnadmin.server.repositorydata.Project;
import com.peterfranza.gwt.svnadmin.server.repositorydata.RepositoryManager;

public class MockRepositoryManager implements RepositoryManager {
		
		@Override
		public void scanForProjects() {}
		
		@Override
		public ChangeSet getSummary(long revisionNumber) {
			return null;
		}
		
		@Override
		public Collection<Project> getProjects() {
			ArrayList<Project> l = new ArrayList<Project>();
			l.add(new Project() {
				
				@Override
				public void unsubscribe(Entity entity) {}
				
				@Override
				public void subscribe(Entity entity) {}
				
				@Override
				public boolean isSubscribed(Entity entity) {
					return false;
				}
				
				@Override
				public String getPath() {
					return null;
				}
				
				@Override
				public String getName() {
					return null;
				}
				
				@Override
				public boolean canWrite(Entity entity) {
					return false;
				}
				
				@Override
				public boolean canRead(Entity entity) {
					return false;
				}
			});
			return l;
		}
		
		@Override
		public Project getProjectForName(String name) {
			return null;
		}
		
		@Override
		public void addProject(Project project) {}
}

