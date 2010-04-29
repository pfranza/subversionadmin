package com.peterfranza.gwt.svnadmin.server.datastore;

import org.hibernate.Session;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class HibernateModule extends AbstractModule {

	private String databaseName;
	private Class<? extends PersistanceSubConfiguration> configClass;
	private String databaseHome;
		
	public HibernateModule(String databaseName, String databaseHome,
			Class<? extends PersistanceSubConfiguration> configClass) {
		super();
		this.databaseName = databaseName;
		this.databaseHome = databaseHome;
		this.configClass = configClass;
	}

	@Override
	protected void configure() {
		bind(String.class).annotatedWith(Names.named("DatabaseName")).toInstance(databaseName);
		bind(String.class).annotatedWith(Names.named("DatabaseHome")).toInstance(databaseHome);
		bind(PersistanceSubConfiguration.class).to(configClass);
		bind(Session.class).toProvider(SessionFactory.class);		
	}

}
