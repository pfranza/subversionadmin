package com.peterfranza.gwt.svnadmin.client.actions.projectmanagement;

import net.customware.gwt.dispatch.shared.Action;

import com.peterfranza.gwt.svnadmin.client.actions.MessageResult;

public class ProjectRemoveUserSubscription implements Action<MessageResult>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4043968302192730384L;
	
	private String project;
	private String memberName;
	
	public ProjectRemoveUserSubscription(String project, String memberName) {
		super();
		this.project = project;
		this.memberName = memberName;
	}
	
	protected ProjectRemoveUserSubscription() {
	}
	
	public String getProject() {
		return project;
	}
	
	public String getMemberName() {
		return memberName;
	}
	
}
