package com.peterfranza.gwt.svnadmin.server.entitydata.local;

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.TestCase;

import com.peterfranza.gwt.svnadmin.server.datastore.HibernateBeanRegistry;
import com.peterfranza.gwt.svnadmin.server.datastore.HibernateSessionFactory;
import com.peterfranza.gwt.svnadmin.server.datastore.InMemoryDatabaseParams;
import com.peterfranza.gwt.svnadmin.server.entitydata.Entity;
import com.peterfranza.gwt.svnadmin.server.entitydata.Group;
import com.peterfranza.gwt.svnadmin.server.entitydata.GroupManager;
import com.peterfranza.gwt.svnadmin.server.entitydata.User;
import com.peterfranza.gwt.svnadmin.server.util.NoCrypt;

public class LocalGroupManagerTest extends TestCase {

	private GroupManager groupManager;
	private LocalUserManager userManager;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		HibernateSessionFactory hbm = new HibernateSessionFactory(
				new InMemoryDatabaseParams("test"), new HibernateBeanRegistry() {				
					@Override
					public Collection<Class<?>> getBeans() {
						Collection<Class<?>> l = new ArrayList<Class<?>>();
						l.add(HbmUserImpl.class);
						l.add(HbmGroupImpl.class);
						return l;
					}
				});
		
		userManager = new LocalUserManager(new NoCrypt(), hbm);
		
		groupManager = new LocalGroupManager(hbm, userManager);
	}
	
	public void testCreateGroup() throws Exception {
		assertEquals(0, groupManager.getGroups().size());
		groupManager.createGroup("testGroup");
		assertEquals(1, groupManager.getGroups().size());
	}
	
	public void testBadCreateGroup() throws Exception {
		groupManager.createGroup("testGroup");
		try {
		groupManager.createGroup("testGroup");
		fail();
		} catch (Exception e) {}
		
	}
	
	public void testRemoveGroup() throws Exception {
		assertEquals(0, groupManager.getGroups().size());
		groupManager.createGroup("testGroup");
		assertEquals(1, groupManager.getGroups().size());
		groupManager.removeGroup("testGroup");
		assertEquals(0, groupManager.getGroups().size());
	}
	
	public void testAddUserMember() throws Exception {
		groupManager.createGroup("testGroup");
		assertEquals(0, groupManager.getGroup("testGroup").getMembers().size());
		userManager.createUser("testUser");
		groupManager.addMemberToGroup("testGroup", createSimpleUser("testUser"));
		assertEquals(1, groupManager.getGroup("testGroup").getMembers().size());
		assertTrue(groupManager.getGroup("testGroup").getMembers().iterator().next() instanceof User);
	}
	
	public void testRemoveUserMember() throws Exception {
		groupManager.createGroup("testGroup");
		assertEquals(0, groupManager.getGroup("testGroup").getMembers().size());
		userManager.createUser("testUser");
		groupManager.addMemberToGroup("testGroup", createSimpleUser("testUser"));
		assertEquals(1, groupManager.getGroup("testGroup").getMembers().size());
		groupManager.removeMemberFromGroup("testGroup", createSimpleUser("testUser"));
		assertEquals(0, groupManager.getGroup("testGroup").getMembers().size());
	}
	
	public void testAddGroupMember() throws Exception {
		groupManager.createGroup("testGroup");
		groupManager.createGroup("testGroup2");
		assertEquals(0, groupManager.getGroup("testGroup").getMembers().size());
		groupManager.addMemberToGroup("testGroup", createSimpleGroup("testGroup2"));
		assertEquals(1, groupManager.getGroup("testGroup").getMembers().size());
		assertTrue(groupManager.getGroup("testGroup").getMembers().iterator().next() instanceof Group);
	}
	
	public void testBadAddGroupMemeber() throws Exception {
		try {
			groupManager.addMemberToGroup("testGroup3", createSimpleGroup("testGroup2"));
			fail();
		} catch (Exception e) {}
	}
	
	public void testBadRemoveGroupMemeber() throws Exception {
		try {
			groupManager.removeMemberFromGroup("testGroup3", createSimpleGroup("testGroup2"));
			fail();
		} catch (Exception e) {}
	}


	public void testRemoveGroupMember() throws Exception {
		groupManager.createGroup("testGroup");
		groupManager.createGroup("testGroup2");
		assertEquals(0, groupManager.getGroup("testGroup").getMembers().size());
		userManager.createUser("testUser");
		groupManager.addMemberToGroup("testGroup", createSimpleGroup("testGroup2"));
		assertEquals(1, groupManager.getGroup("testGroup").getMembers().size());
		groupManager.removeMemberFromGroup("testGroup", createSimpleGroup("testGroup2"));
		assertEquals(0, groupManager.getGroup("testGroup").getMembers().size());
	}

	private Entity createSimpleUser(String string) {
		HbmUserImpl u = new HbmUserImpl();
		u.setName(string);
		return u;
	}
	
	private Entity createSimpleGroup(String string) {
		HbmGroupImpl u = new HbmGroupImpl();
		u.setName(string);
		return u;
	}
	
	public void testBadGetMemeber() throws Exception {
		try {
			HbmGroupImpl i = new HbmGroupImpl();
			i.getMembers();
			fail();
		} catch (Exception e) {}
	}
}
