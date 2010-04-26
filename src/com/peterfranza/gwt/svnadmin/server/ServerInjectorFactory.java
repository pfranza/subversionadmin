package com.peterfranza.gwt.svnadmin.server;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;
import com.google.inject.name.Names;
import com.peterfranza.gwt.svnadmin.server.datastore.HibernateSessionFactory;
import com.peterfranza.gwt.svnadmin.server.datastore.InMemoryDatabaseParams;
import com.peterfranza.gwt.svnadmin.server.datastore.PersistanceSubConfiguration;
import com.peterfranza.gwt.svnadmin.server.entitydata.local.LocalGroupModule;
import com.peterfranza.gwt.svnadmin.server.entitydata.local.LocalUserModule;
import com.peterfranza.gwt.svnadmin.server.repositorydata.svn.SvnManagementModule;
import com.peterfranza.gwt.svnadmin.server.util.ConfigFileWriter;

public class ServerInjectorFactory {

	private List<Module> modules = new ArrayList<Module>();
	
	{
		modules.add(new SvnManagementModule());
		modules.add(new LocalUserModule());
		modules.add(new LocalGroupModule());
		modules.add(new AbstractModule() {
			@Override
			protected void configure() {
				bind(String.class).annotatedWith(Names.named("DatabaseName")).toInstance("TestDB");
				bind(String.class).annotatedWith(Names.named("repositoryUrl")).toInstance("http://svn/svn/");
				bind(String.class).annotatedWith(Names.named("repositoryUsername")).toInstance("hudson");
				bind(String.class).annotatedWith(Names.named("repositoryPassword")).toInstance("ib.hudson");
				
				bind(ConfigFileWriter.class).annotatedWith(Names.named("passwordFile")).toInstance(new ConfigFileWriter(new File("svnpasswordz")));
				bind(ConfigFileWriter.class).annotatedWith(Names.named("authorsFile")).toInstance(new ConfigFileWriter(new File("svnauthorz")));
				
				bind(PersistanceSubConfiguration.class).to(InMemoryDatabaseParams.class);
				bind(Session.class).toProvider(HibernateSessionFactory.class);
			}
		});
	}
	
	public Injector getInjector() {
		return Guice.createInjector(Stage.PRODUCTION, modules);
	}
	
}
