package com.peterfranza.gwt.svnadmin.server.repositorydata.svn;

import com.google.inject.AbstractModule;
import com.peterfranza.gwt.svnadmin.server.datastore.AbstractHibernateBeanModule;
import com.peterfranza.gwt.svnadmin.server.repositorydata.RepositoryManager;

public class SvnManagementModule extends AbstractModule {

	protected void configure() {
		bind(RepositoryManager.class).to(SvnRepositoryManager.class);
		install(new AbstractHibernateBeanModule() {
			
			@Override
			protected void configureBeans() {
				bindBean(SvnProjectBean.class);
			}
		});
	}
	
}
