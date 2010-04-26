package com.peterfranza.gwt.svnadmin.server.entitydata.local;

import junit.framework.TestCase;

import org.hibernate.Session;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import com.peterfranza.gwt.svnadmin.server.MockRepositoryManager;
import com.peterfranza.gwt.svnadmin.server.datastore.HibernateSessionFactory;
import com.peterfranza.gwt.svnadmin.server.datastore.InMemoryDatabaseParams;
import com.peterfranza.gwt.svnadmin.server.datastore.PersistanceSubConfiguration;
import com.peterfranza.gwt.svnadmin.server.entitydata.UserManager;
import com.peterfranza.gwt.svnadmin.server.repositorydata.RepositoryManager;
import com.peterfranza.gwt.svnadmin.server.util.ConfigFileWriter;
import com.peterfranza.gwt.svnadmin.server.util.NullWriter;

public class ModulesTest extends TestCase {

	public void testModules() throws Exception {
		Injector i = Guice.createInjector(new LocalGroupModule(), new LocalUserModule(), new AbstractModule() {
			
			@Override
			protected void configure() {
				bind(PersistanceSubConfiguration.class).toInstance(new InMemoryDatabaseParams("test"));
				bind(Session.class).toProvider(HibernateSessionFactory.class);
				
				bind(ConfigFileWriter.class).annotatedWith(Names.named("passwordFile")).toInstance(new NullWriter());
				bind(ConfigFileWriter.class).annotatedWith(Names.named("authorsFile")).toInstance(new NullWriter());
				
				bind(RepositoryManager.class).toInstance(new MockRepositoryManager());
				
			}
		});
		UserManager um = i.getInstance(UserManager.class);
		assertEquals(0, um.getUsers().size());
	}
}
