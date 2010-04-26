package com.peterfranza.gwt.svnadmin.server.entitydata.local;

import junit.framework.TestCase;

import org.hibernate.Session;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.peterfranza.gwt.svnadmin.server.datastore.HibernateSessionFactory;
import com.peterfranza.gwt.svnadmin.server.datastore.InMemoryDatabaseParams;
import com.peterfranza.gwt.svnadmin.server.datastore.PersistanceSubConfiguration;
import com.peterfranza.gwt.svnadmin.server.entitydata.UserManager;

public class ModulesTest extends TestCase {

	public void testModules() throws Exception {
		Injector i = Guice.createInjector(new LocalGroupModule(), new LocalUserModule(), new AbstractModule() {
			
			@Override
			protected void configure() {
				bind(PersistanceSubConfiguration.class).toInstance(new InMemoryDatabaseParams("test"));
				bind(Session.class).toProvider(HibernateSessionFactory.class);				
			}
		});
		UserManager um = i.getInstance(UserManager.class);
		assertEquals(0, um.getUsers().size());
	}
}
