package com.peterfranza.gwt.svnadmin.server.repositorydata.svn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import junit.framework.TestCase;

import com.peterfranza.gwt.svnadmin.server.datastore.HibernateBeanRegistry;
import com.peterfranza.gwt.svnadmin.server.datastore.HibernateSessionFactory;
import com.peterfranza.gwt.svnadmin.server.datastore.InMemoryDatabaseParams;
import com.peterfranza.gwt.svnadmin.server.datastore.SessionFactory;
import com.peterfranza.gwt.svnadmin.server.entitydata.Entity;
import com.peterfranza.gwt.svnadmin.server.entitydata.Group;
import com.peterfranza.gwt.svnadmin.server.entitydata.User;
import com.peterfranza.gwt.svnadmin.server.repositorydata.ProjectDataWriter;
import com.peterfranza.gwt.svnadmin.server.repositorydata.RepositoryManager;
import com.peterfranza.gwt.svnadmin.server.repositorydata.RepositoryManager.ACCESS;

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
		}, new SessionFactory(hbm), new ProjectDataWriter() {			
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
	
	public void testCanRead() throws Exception {
		reposManager.addProject("testProj");
		assertFalse(reposManager.canRead("testProj", createUser("testUser")));
		reposManager.setReadWrite("testProj", createUser("testUser"), ACCESS.READ);
		assertTrue(reposManager.canRead("testProj", createUser("testUser")));
		reposManager.setReadWrite("testProj", createUser("testUser"), ACCESS.WRITE);
		assertTrue(reposManager.canRead("testProj", createUser("testUser")));
		reposManager.setReadWrite("testProj", createUser("testUser"), ACCESS.NONE);
		assertFalse(reposManager.canRead("testProj", createUser("testUser")));
	}
	
	public void testCanWrite() throws Exception {
		reposManager.addProject("testProj");
		assertFalse(reposManager.canWrite("testProj", createUser("testUser")));
		reposManager.setReadWrite("testProj", createUser("testUser"), ACCESS.WRITE);
		assertTrue(reposManager.canWrite("testProj", createUser("testUser")));
		reposManager.setReadWrite("testProj", createUser("testUser"), ACCESS.READ);
		assertFalse(reposManager.canWrite("testProj", createUser("testUser")));
		reposManager.setReadWrite("testProj", createUser("testUser"), ACCESS.NONE);
		assertFalse(reposManager.canWrite("testProj", createUser("testUser")));
	}
	
	public void testGroupCanWrite() throws Exception {
		reposManager.addProject("testProj");
		assertFalse(reposManager.canWrite("testProj", createGroup("testGroup")));
		reposManager.setReadWrite("testProj", createGroup("testGroup"), ACCESS.WRITE);
		assertTrue(reposManager.canWrite("testProj", createGroup("testGroup")));
		reposManager.setReadWrite("testProj", createGroup("testGroup"), ACCESS.READ);
		assertFalse(reposManager.canWrite("testProj", createGroup("testGroup")));
		reposManager.setReadWrite("testProj", createGroup("testGroup"), ACCESS.NONE);
		assertFalse(reposManager.canWrite("testProj", createGroup("testGroup")));
	}

	public void testSubscription() throws Exception {
		reposManager.addProject("testProj");
		assertFalse(reposManager.isSubscribed("testProj", createUser("test1")));
		reposManager.subscribe("testProj", createUser("test1"));
		assertTrue(reposManager.isSubscribed("testProj", createUser("test1")));
		reposManager.unsubscribe("testProj", createUser("test1"));
		assertFalse(reposManager.isSubscribed("testProj", createUser("test1")));
	}

	private Entity createGroup(final String string) {
		return new Group() {
			
			@Override
			public String getName() {
				return string;
			}
			
			@Override
			public Collection<Entity> getMembers() {
				return new ArrayList<Entity>();
			}
		};
	}
	
	private User createUser(final String string) {
		return new User() {			
			@Override
			public String getName() {
				return string;
			}

			@Override
			public String getEmailAddress() {
				return null;
			}

			@Override
			public String getPassword() {
				return null;
			}

			@Override
			public boolean isAdministrator() {
				return false;
			}

			@Override
			public void setAdministrator(boolean isAdministrator) {}

			@Override
			public void setEmailAddress(String emailAddress) {}

			@Override
			public void setPassword(String password) {}
		};
	}
	
}
