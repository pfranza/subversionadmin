package com.peterfranza.gwt.svnadmin.client.actions.projectmanagement;

import net.customware.gwt.dispatch.shared.Action;

import com.peterfranza.gwt.svnadmin.client.actions.MessageResult;

public class ProjectAddUserSubscription implements Action<MessageResult>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7954875155414627029L;
	
	private String project;
	private String memberName;
	
	public ProjectAddUserSubscription(String project, String memberName) {
		super();
		this.project = project;
		this.memberName = memberName;
	}
	
	protected ProjectAddUserSubscription() {
	}
	
	public String getProject() {
		return project;
	}
	
	public String getMemberName() {
		return memberName;
	}
	
}
