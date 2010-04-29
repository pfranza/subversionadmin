package com.peterfranza.gwt.svnadmin.server;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.customware.gwt.dispatch.server.service.DispatchServiceServlet;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;
import com.google.inject.servlet.ServletModule;
import com.peterfranza.gwt.svnadmin.server.datastore.HibernateModule;
import com.peterfranza.gwt.svnadmin.server.datastore.InMemoryDatabaseParams;
import com.peterfranza.gwt.svnadmin.server.entitydata.local.LocalGroupModule;
import com.peterfranza.gwt.svnadmin.server.entitydata.local.LocalUserModule;
import com.peterfranza.gwt.svnadmin.server.handlers.HandlerBinding;
import com.peterfranza.gwt.svnadmin.server.mailer.MailerModule;
import com.peterfranza.gwt.svnadmin.server.repositorydata.svn.SvnManagementModule;
import com.peterfranza.gwt.svnadmin.server.util.ConfigFileWriter;

public class ServerInjectorFactory {

	private List<Module> modules = new ArrayList<Module>();
	
	{
		modules.add(new SvnManagementModule("http://subversionadmin.googlecode.com/svn/", "", "", new ConfigFileWriter(new File("svnauthorz"))));
		modules.add(new LocalUserModule(new ConfigFileWriter(new File("svnpasswordz"))));
//		modules.add(new LDAPUserModule("", "", "", ""));
		modules.add(new LocalGroupModule());
		modules.add(new HandlerBinding());
		modules.add(new MailerModule("mail", "svn@test.com"));
		modules.add(new HibernateModule("subversionAdmin", InMemoryDatabaseParams.class));
		modules.add(new ServletModule() {
			@Override
			protected void configureServlets() {
				serve("/subversionadministrator/dispatch" ).with( DispatchServiceServlet.class );
			}
		});
	}
	
	public Injector getInjector() {
		return Guice.createInjector(Stage.PRODUCTION, modules);
	}
	
}
