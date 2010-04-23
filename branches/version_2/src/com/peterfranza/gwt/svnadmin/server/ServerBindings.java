package com.peterfranza.gwt.svnadmin.server;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;
import com.google.inject.name.Names;
import com.google.inject.servlet.GuiceServletContextListener;
import com.peterfranza.gwt.svnadmin.server.datastore.HibernateSessionFactory;
import com.peterfranza.gwt.svnadmin.server.datastore.InMemoryDatabaseParams;
import com.peterfranza.gwt.svnadmin.server.datastore.PersistanceSubConfiguration;

public class ServerBindings extends GuiceServletContextListener {

	private List<Module> modules = new ArrayList<Module>();
	
	{
		modules.add(new AbstractModule() {
			@Override
			protected void configure() {
				bind(String.class).annotatedWith(
						Names.named("DatabaseName"))
						.toInstance("TestDB");
				bind(PersistanceSubConfiguration.class).to(InMemoryDatabaseParams.class);
				bind(Session.class).toProvider(HibernateSessionFactory.class);
			}
		});
	}
	
	@Override
	protected Injector getInjector() {
		return Guice.createInjector(Stage.PRODUCTION, modules);
	}


}
