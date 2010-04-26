package com.peterfranza.gwt.svnadmin.server.repositorydata.svn;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.peterfranza.gwt.svnadmin.server.entitydata.User;
import com.peterfranza.gwt.svnadmin.server.entitydata.UserManager;
import com.peterfranza.gwt.svnadmin.server.repositorydata.Project;
import com.peterfranza.gwt.svnadmin.server.repositorydata.ProjectDataWriter;
import com.peterfranza.gwt.svnadmin.server.repositorydata.RepositoryManager;
import com.peterfranza.gwt.svnadmin.server.util.ConfigFileWriter;

public class DefaultProjectDataWriter implements ProjectDataWriter {

	private ConfigFileWriter authorsFileWriter;
	private RepositoryManager reposManager;
	private UserManager userManager;
	
	@Inject
	public DefaultProjectDataWriter(UserManager userManager,
			@Named("authorsFile") ConfigFileWriter authorsFileWriter,
			RepositoryManager reposManager) {
		this.userManager = userManager;
		this.authorsFileWriter = authorsFileWriter;
		this.reposManager = reposManager;
	}
	
	@Override
	public void saveData() {
		StringBuffer buf = new StringBuffer();
		
		buf.append(System.getProperty("line.separator"));
		buf.append(System.getProperty("line.separator"));
		
		buf.append("[/]").append(System.getProperty("line.separator"));
		buf.append("* = r").append(System.getProperty("line.separator"));
		for(User u: userManager.getUsers()) {
			if(u.isAdministrator()) {
				buf.append(u.getName()).append(" = rw").append(System.getProperty("line.separator"));
			}
		}

		for(Project p: reposManager.getProjects()) {
			buf.append(System.getProperty("line.separator"));
			buf.append("[/"+p.getPath()+"]").append(System.getProperty("line.separator"));
			for(User u: userManager.getUsers()) {
				if(reposManager.canWrite(p.getPath(), u)) {
					buf.append(u.getName()).append(" = rw").append(System.getProperty("line.separator"));
				} else if(reposManager.canRead(p.getPath(), u)) {
					buf.append(u.getName()).append(" = r").append(System.getProperty("line.separator"));
				} else {
					buf.append(u.getName()).append(" = ").append(System.getProperty("line.separator"));
				}
			}
			
		}
		
		authorsFileWriter.save(buf.toString().trim());
	}
	
}
