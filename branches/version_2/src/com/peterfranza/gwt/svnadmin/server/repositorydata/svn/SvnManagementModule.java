package com.peterfranza.gwt.svnadmin.server.repositorydata.svn;

import org.tmatesoft.svn.core.io.SVNRepository;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.peterfranza.gwt.svnadmin.server.datastore.AbstractHibernateBeanModule;
import com.peterfranza.gwt.svnadmin.server.repositorydata.ProjectDataWriter;
import com.peterfranza.gwt.svnadmin.server.repositorydata.RepositoryManager;
import com.peterfranza.gwt.svnadmin.server.util.ConfigFileWriter;

public class SvnManagementModule extends AbstractModule {

	private String repositoryUrl;
	private String repositoryUsername;
	private String repositoryPassword;
	private ConfigFileWriter authorzWriter;

	public SvnManagementModule(String repositoryUrl, 
			String repositoryUsername,
			String repositoryPassword,
			ConfigFileWriter authorzWriter) {
		this.repositoryUrl = repositoryUrl;
		this.repositoryUsername = repositoryUsername;
		this.authorzWriter = authorzWriter;
		this.repositoryPassword = repositoryPassword;
		
		System.out.println("Managing Repository " + repositoryUsername + "@" + repositoryUrl);
		
	}
	
	protected void configure() {
		bind(RepositoryManager.class).to(SvnRepositoryManager.class).asEagerSingleton();
		bind(SVNRepository.class).toProvider(RepositoryFactory.class);
		bind(ProjectDataWriter.class).to(DefaultProjectDataWriter.class);
		
		bind(String.class).annotatedWith(Names.named("repositoryUrl")).toInstance(repositoryUrl);
		bind(String.class).annotatedWith(Names.named("repositoryUsername")).toInstance(repositoryUsername);
		bind(String.class).annotatedWith(Names.named("repositoryPassword")).toInstance(repositoryPassword);
		bind(ConfigFileWriter.class).annotatedWith(Names.named("authorsFile")).toInstance(authorzWriter);
				
		install(new AbstractHibernateBeanModule() {			
			@Override
			protected void configureBeans() {
				bindBean(SvnProjectBean.class);
			}
		});
	}
	
}
