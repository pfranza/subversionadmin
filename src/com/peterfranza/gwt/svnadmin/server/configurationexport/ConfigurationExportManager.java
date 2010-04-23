package com.peterfranza.gwt.svnadmin.server.configurationexport;

import com.peterfranza.gwt.svnadmin.server.entitydata.groupdata.GroupManager;
import com.peterfranza.gwt.svnadmin.server.entitydata.userdata.UserManager;
import com.peterfranza.gwt.svnadmin.server.repositorydata.RepositoryManager;

public interface ConfigurationExportManager {

	void exportPasswordFile(UserManager userManager);
	void exportPermissionsFile(UserManager userManager, 
			GroupManager groupManager,
			RepositoryManager repositoryManager);
	
}
