package com.peterfranza.gwt.svnadmin.server.repositorydata.svn;

import org.tmatesoft.svn.core.io.SVNRepository;

import com.google.inject.AbstractModule;
import com.peterfranza.gwt.svnadmin.server.datastore.AbstractHibernateBeanModule;
import com.peterfranza.gwt.svnadmin.server.repositorydata.ProjectDataWriter;
import com.peterfranza.gwt.svnadmin.server.repositorydata.RepositoryManager;

public class SvnManagementModule extends AbstractModule {

	protected void configure() {
		bind(RepositoryManager.class).to(SvnRepositoryManager.class);
		bind(SVNRepository.class).toProvider(RepositoryFactory.class);
		bind(ProjectDataWriter.class).to(DefaultProjectDataWriter.class);
		
		install(new AbstractHibernateBeanModule() {
			
			@Override
			protected void configureBeans() {
				bindBean(SvnProjectBean.class);
			}
		});
	}
	
}
