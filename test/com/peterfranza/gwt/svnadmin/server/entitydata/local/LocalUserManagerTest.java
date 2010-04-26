package com.peterfranza.gwt.svnadmin.server.entitydata.local;

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.TestCase;

import com.peterfranza.gwt.svnadmin.server.datastore.HibernateBeanRegistry;
import com.peterfranza.gwt.svnadmin.server.datastore.HibernateSessionFactory;
import com.peterfranza.gwt.svnadmin.server.datastore.InMemoryDatabaseParams;
import com.peterfranza.gwt.svnadmin.server.entitydata.UserManager;
import com.peterfranza.gwt.svnadmin.server.util.NoCrypt;
import com.peterfranza.gwt.svnadmin.server.util.NullWriter;

public class LocalUserManagerTest extends TestCase {

	private UserManager userManager;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		userManager = new LocalUserManager(new NoCrypt(), new HibernateSessionFactory(
				new InMemoryDatabaseParams("test"), new HibernateBeanRegistry() {				
					@Override
					public Collection<Class<?>> getBeans() {
						Collection<Class<?>> l = new ArrayList<Class<?>>();
						l.add(HbmUserImpl.class);
						return l;
					}
				}), new NullWriter());
	}
	
	public void testCreateUser() throws Exception {
		assertEquals(0, userManager.getUsers().size());
		userManager.createUser("testUser");
		assertEquals(1, userManager.getUsers().size());
	}
	
	public void testRemoveUser() throws Exception {
		assertEquals(0, userManager.getUsers().size());
		userManager.createUser("testUser");
		assertEquals(1, userManager.getUsers().size());
		userManager.removeUser("testUser");
		assertEquals(0, userManager.getUsers().size());
	}
	
	public void testGetUserForName() throws Exception {
		assertEquals(0, userManager.getUsers().size());
		userManager.createUser("testUser");
		assertEquals(1, userManager.getUsers().size());
		assertEquals("testUser", userManager.getUserForName("testUser").getName());
		userManager.removeUser("testUser");
		assertEquals(0, userManager.getUsers().size());
	}
	
	public void testSetPassword() throws Exception {
		assertEquals(0, userManager.getUsers().size());
		userManager.createUser("testUser");
		assertEquals(1, userManager.getUsers().size());
		userManager.setPassword("testUser", "testPassword");
		assertEquals("testPassword", userManager.getUserForName("testUser").getPassword());
		userManager.removeUser("testUser");
		assertEquals(0, userManager.getUsers().size());
	}
	
	public void testSetAdministrator() throws Exception {
		assertEquals(0, userManager.getUsers().size());
		userManager.createUser("testUser");
		assertEquals(1, userManager.getUsers().size());
		userManager.setAdministrator("testUser", true);
		assertEquals(true, userManager.getUserForName("testUser").isAdministrator());
		userManager.setAdministrator("testUser", false);
		assertEquals(false, userManager.getUserForName("testUser").isAdministrator());
		userManager.removeUser("testUser");
		assertEquals(0, userManager.getUsers().size());
	}
	
	public void testSetEmail() throws Exception {
		assertEquals(0, userManager.getUsers().size());
		userManager.createUser("testUser");
		assertEquals(1, userManager.getUsers().size());
		userManager.setEmailAddress("testUser", "test@email.com");
		assertEquals("test@email.com", userManager.getUserForName("testUser").getEmailAddress());
		userManager.removeUser("testUser");
		assertEquals(0, userManager.getUsers().size());
	}
	
	public void testAuthenticate() throws Exception {
		assertEquals(0, userManager.getUsers().size());
		userManager.createUser("testUser");
		assertEquals(1, userManager.getUsers().size());
		userManager.setPassword("testUser", "testPassword");
		assertEquals("testPassword", userManager.getUserForName("testUser").getPassword());
		assertTrue(userManager.authenticate("testUser", "testPassword"));
		assertFalse(userManager.authenticate("testUser", "badPassword"));
		assertFalse(userManager.authenticate("badUser", ""));
		userManager.removeUser("testUser");
		assertEquals(0, userManager.getUsers().size());
	}
	
	public void testFailSetEmail() throws Exception {
		try {
			userManager.setEmailAddress("badUser", "test@email.com");
			fail();
		} catch (RuntimeException e) {}
	}
	
	public void testCreateDupUser() throws Exception {
		assertEquals(0, userManager.getUsers().size());
		userManager.createUser("testUser");
		assertEquals(1, userManager.getUsers().size());
		try {
			userManager.createUser("testUser");
			fail();
		} catch (RuntimeException e) {}
	}
	
}
