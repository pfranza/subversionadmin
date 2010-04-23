package com.peterfranza.gwt.svnadmin.server.configurationexport;

import com.google.inject.Inject;
import com.peterfranza.gwt.svnadmin.server.entitydata.GroupManager;
import com.peterfranza.gwt.svnadmin.server.entitydata.UserManager;
import com.peterfranza.gwt.svnadmin.server.repositorydata.RepositoryManager;

public class DefaultConfigurationExportManager implements ConfigurationExportManager{

	@Inject
	public DefaultConfigurationExportManager(
			UserManager userManager, 
			GroupManager groupManager,
			RepositoryManager repositoryManager) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void exportPasswordFile() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exportPermissionsFile() {
		// TODO Auto-generated method stub
		
	}
	


}
