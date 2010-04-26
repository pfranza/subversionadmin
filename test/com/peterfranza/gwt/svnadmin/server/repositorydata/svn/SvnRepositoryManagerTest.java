package com.peterfranza.gwt.svnadmin.server.repositorydata.svn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import junit.framework.TestCase;

import com.peterfranza.gwt.svnadmin.server.datastore.HibernateBeanRegistry;
import com.peterfranza.gwt.svnadmin.server.datastore.HibernateSessionFactory;
import com.peterfranza.gwt.svnadmin.server.datastore.InMemoryDatabaseParams;
import com.peterfranza.gwt.svnadmin.server.repositorydata.ProjectDataWriter;
import com.peterfranza.gwt.svnadmin.server.repositorydata.RepositoryManager;

public class SvnRepositoryManagerTest extends TestCase {

	private RepositoryManager reposManager;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		HibernateSessionFactory hbm = new HibernateSessionFactory(
				new InMemoryDatabaseParams("test"), new HibernateBeanRegistry() {				
					@Override
					public Collection<Class<?>> getBeans() {
						Collection<Class<?>> l = new ArrayList<Class<?>>();
						l.add(SvnProjectBean.class);
						return l;
					}
				});
		
		reposManager = new SvnRepositoryManager(new ProjectScanner() {
			
			@Override
			public List<String> getProjectPaths(Collection<String> knownPaths) {
				ArrayList<String> list = new ArrayList<String>();
				list.add("proj1/");
				list.add("proj2/");
				list.add("proj3/");
				return list;
			}
		}, hbm, new ProjectDataWriter() {			
			@Override
			public void saveData() {}
		});
	}
	
	public void testAddProject() throws Exception {
		assertEquals(0, reposManager.getProjects().size());
		reposManager.addProject("testProj");
		assertEquals(1, reposManager.getProjects().size());
	}
	
	public void testGetProject() throws Exception {
		assertNull(reposManager.getProjectForName("testProj"));
		reposManager.addProject("testProj");
		assertNotNull(reposManager.getProjectForName("testProj"));
	}
	
	public void testScanForProjects() throws Exception {
		reposManager.addProject("badProj");
		reposManager.addProject("proj3/");
		assertEquals(2, reposManager.getProjects().size());
		reposManager.scanForProjects();
		assertEquals(3, reposManager.getProjects().size());
	}
	
}
