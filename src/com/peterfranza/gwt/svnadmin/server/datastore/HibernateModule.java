package com.peterfranza.gwt.svnadmin.server.datastore;

import org.hibernate.Session;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class HibernateModule extends AbstractModule {

	private String databaseName;
	private Class<? extends PersistanceSubConfiguration> configClass;
		
	public HibernateModule(String databaseName,
			Class<? extends PersistanceSubConfiguration> configClass) {
		super();
		this.databaseName = databaseName;
		this.configClass = configClass;
	}

	@Override
	protected void configure() {
		bind(String.class).annotatedWith(Names.named("DatabaseName")).toInstance(databaseName);
		bind(PersistanceSubConfiguration.class).to(configClass);
		bind(Session.class).toProvider(SessionFactory.class);		
	}

}
