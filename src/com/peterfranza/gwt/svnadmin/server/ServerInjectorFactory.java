package com.peterfranza.gwt.svnadmin.server;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import net.customware.gwt.dispatch.server.service.DispatchServiceServlet;

import org.apache.commons.configuration.Configuration;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;
import com.google.inject.servlet.ServletModule;
import com.peterfranza.gwt.svnadmin.server.datastore.HSqlDbParams;
import com.peterfranza.gwt.svnadmin.server.datastore.HibernateModule;
import com.peterfranza.gwt.svnadmin.server.entitydata.ldap.LDAPUserModule;
import com.peterfranza.gwt.svnadmin.server.entitydata.local.LocalGroupModule;
import com.peterfranza.gwt.svnadmin.server.entitydata.local.LocalUserModule;
import com.peterfranza.gwt.svnadmin.server.handlers.HandlerBinding;
import com.peterfranza.gwt.svnadmin.server.mailer.MailerModule;
import com.peterfranza.gwt.svnadmin.server.repositorydata.svn.SvnManagementModule;
import com.peterfranza.gwt.svnadmin.server.util.ConfigFileWriter;

public class ServerInjectorFactory {

	private List<Module> modules = new ArrayList<Module>();
	
	public ServerInjectorFactory(ServletContext servletContext){
		final Configuration cfg = new ConfigurationFactory(servletContext).get();
		
		System.out.println(cfg.getString("svnadministrator.svn.authorsfile"));
		
		modules.add(new SvnManagementModule(
				cfg.getString("svnadministrator.svn.url"),
				cfg.getString("svnadministrator.svn.username", ""),
				cfg.getString("svnadministrator.svn.password", ""),
				new ConfigFileWriter(new File(cfg.getString("svnadministrator.svn.authorsfile")))));
		
		if(cfg.getBoolean("svnadministrator.users.useActiveDirectory", false)) {
			modules.add(new LDAPUserModule(
					cfg.getString("svnadministrator.users.ad.url"), 
					cfg.getString("svnadministrator.users.ad.username", ""), 
					cfg.getString("svnadministrator.users.ad.password", ""), 
					cfg.getString("svnadministrator.users.ad.admingroup", ""),
					cfg.getString("svnadministrator.users.ad.query"),
					cfg.getStringArray("svnadministrator.users.ad.query.attr"),
					cfg.getString("svnadministrator.users.ad.query.usernamekey"),
					cfg.getString("svnadministrator.users.ad.query.mailkey"),
					cfg.getString("svnadministrator.users.ad.query.principlekey"),
					cfg.getString("svnadministrator.users.ad.query.memberofkey")
					));
		} else {
			modules.add(new LocalUserModule(new ConfigFileWriter(
					new File(cfg.getString("svnadministrator.svn.passwordsfile")))));
		}
		
		if(cfg.getBoolean("svnadministrator.email.enable", true)) {
			modules.add(new MailerModule(
					cfg.getString("svnadministrator.email.smtpserver", "mail"), 
					cfg.getString("svnadministrator.email.defaultsender", "svn-changes@localhost.com")));
		}
		
		
		modules.add(new HibernateModule(
				"SubversionAdminData",
				cfg.getString("svnadministrator.config.home", System.getProperty("user.dir")),
				HSqlDbParams.class));
		
		modules.add(new LocalGroupModule());
		modules.add(new HandlerBinding());
		modules.add(new ServletModule() {
			@Override
			protected void configureServlets() {
				bind(Configuration.class).toInstance(cfg);
				serve("/subversionadministrator/dispatch" ).with( DispatchServiceServlet.class );
			}
		});
	}
	
	public Injector getInjector() {
		return Guice.createInjector(Stage.PRODUCTION, modules);
	}
	
}
