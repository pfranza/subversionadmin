package com.peterfranza.gwt.svnadmin.server.datastore;

import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Environment;
import org.hibernate.context.ThreadLocalSessionContext;
import org.hibernate.dialect.HSQLDialect;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class InMemoryDatabaseParams implements PersistanceSubConfiguration {

	private final String dbName;
	private final String hbm2ddl = "create";
	private final String username = "sa";
	private final String password = "";

	@Inject
	public InMemoryDatabaseParams(@Named("DatabaseName") String name) {
		dbName = name;
	}

	@Override
	public void configure(AnnotationConfiguration config) {

		config
		.setProperty(Environment.DIALECT, HSQLDialect.class.getName())
		.setProperty(Environment.CURRENT_SESSION_CONTEXT_CLASS,	ThreadLocalSessionContext.class.getName())
		.setProperty(Environment.HBM2DDL_AUTO, hbm2ddl)
		.setProperty("hibernate.connection.driver_class", "org.hsqldb.jdbcDriver")

		.setProperty("hibernate.connection.url", "jdbc:hsqldb:mem:" + dbName)
		.setProperty("hibernate.connection.username", username)
		.setProperty("hibernate.connection.password", password);
	}

}
