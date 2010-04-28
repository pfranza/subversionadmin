package com.peterfranza.gwt.svnadmin.client.actions.projectmanagement;

import net.customware.gwt.dispatch.shared.Action;

import com.peterfranza.gwt.svnadmin.client.actions.MessageResult;

public class AddProjectMember implements Action<MessageResult>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4312467845312871168L;
	
	private String project;
	private String memberName;
	
	public AddProjectMember(String project, String memberName) {
		super();
		this.project = project;
		this.memberName = memberName;
	}
	
	protected AddProjectMember() {
	}
	
	public String getProject() {
		return project;
	}
	
	public String getMemberName() {
		return memberName;
	}
	
	
}
