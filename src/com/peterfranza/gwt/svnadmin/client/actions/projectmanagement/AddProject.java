package com.peterfranza.gwt.svnadmin.client.actions.projectmanagement;

import com.peterfranza.gwt.svnadmin.client.actions.MessageResult;

import net.customware.gwt.dispatch.shared.Action;

public class AddProject implements Action<MessageResult>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8264463058868668620L;

	private String projectPath;

	public AddProject(String projectPath) {
		super();
		this.projectPath = projectPath;
	}

	public AddProject() {}
	
	public String getProjectPath() {
		return projectPath;
	}
	
}
