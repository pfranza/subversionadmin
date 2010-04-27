package com.peterfranza.gwt.svnadmin.client.actions;

import net.customware.gwt.dispatch.shared.Action;

public class AddProjectRequest implements Action<MessageResult>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8264463058868668620L;

	private String projectPath;

	public AddProjectRequest(String projectPath) {
		super();
		this.projectPath = projectPath;
	}

	public AddProjectRequest() {}
	
	public String getProjectPath() {
		return projectPath;
	}
	
}
