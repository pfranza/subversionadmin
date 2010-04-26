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
				public String getPath() {
					return "/test";
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

		@Override
		public boolean canRead(String name, Entity entity) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean canWrite(String name, Entity entity) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isSubscribed(String name, Entity entity) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void setReadWrite(String name, Entity entity, ACCESS access) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void subscribe(String name, Entity entity) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void unsubscribe(String name, Entity entity) {
			// TODO Auto-generated method stub
			
		}
}

