package com.peterfranza.gwt.svnadmin.server.configurationexport;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.peterfranza.gwt.svnadmin.server.entitydata.GroupManager;
import com.peterfranza.gwt.svnadmin.server.entitydata.User;
import com.peterfranza.gwt.svnadmin.server.entitydata.UserManager;
import com.peterfranza.gwt.svnadmin.server.repositorydata.RepositoryManager;
import com.peterfranza.gwt.svnadmin.server.util.ConfigFileWriter;

public class DefaultConfigurationExportManager implements ConfigurationExportManager{

	private UserManager userManager;
	private ConfigFileWriter passwordFileWriter;
	private ConfigFileWriter authorsFileWriter;

	@Inject
	public DefaultConfigurationExportManager(
			UserManager userManager, 
			GroupManager groupManager,
			RepositoryManager repositoryManager,
			@Named("passwordFile") ConfigFileWriter passwordFileWriter,
			@Named("authorsFile") ConfigFileWriter authorsFileWriter) {
		this.userManager = userManager;
		this.passwordFileWriter = passwordFileWriter;
		this.authorsFileWriter = authorsFileWriter;
	}

	@Override
	public void exportPasswordFile() {
		StringBuffer buf = new StringBuffer();
		for(User u: userManager.getUsers()) {
			buf.append(u.getName()).append(":").append(u.getPassword()).append(System.getProperty("line.separator"));
		}
		passwordFileWriter.save(buf.toString().trim());
	}

	@Override
	public void exportPermissionsFile() {
		// TODO Auto-generated method stub
		
	}
	


}
