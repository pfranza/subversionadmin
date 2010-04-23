package com.peterfranza.gwt.svnadmin.server.datastore;

import java.io.File;

import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.HSQLDialect;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class HSqlDbParams implements PersistanceSubConfiguration {

	private final String dbName;
	private final String hbm2ddl = "validate";
	private final String username = "sa";
	private final String password = "";

	@Inject
	public HSqlDbParams(@Named("DatabaseName") String name) {
		dbName = name;
	}

	@Override
	public void configure(AnnotationConfiguration config) {

		File f = new File(new File(System.getProperty("user.dir")), dbName);
		config.setProperty(Environment.DIALECT, HSQLDialect.class.getName())
		.setProperty(Environment.CURRENT_SESSION_CONTEXT_CLASS,
		"org.hibernate.context.ThreadLocalSessionContext")
		.setProperty(Environment.HBM2DDL_AUTO, hbm2ddl).setProperty(
				"hibernate.connection.driver_class",
		"org.hsqldb.jdbcDriver")

		.setProperty("hibernate.connection.url",
				"jdbc:hsqldb:file:" + f.getAbsolutePath()+";hsqldb.default_table_type=cached").setProperty(
						"hibernate.connection.username", username).setProperty(
								"hibernate.connection.password", password);
	}

}
