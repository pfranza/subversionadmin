package com.peterfranza.gwt.svnadmin.client.actions.projectmanagement;

import net.customware.gwt.dispatch.shared.Action;

import com.peterfranza.gwt.svnadmin.client.actions.MessageResult;

public class RemoveProjectMember implements Action<MessageResult>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8236955379621229580L;
	
	private String project;
	private String memberName;
	
	public RemoveProjectMember(String project, String memberName) {
		super();
		this.project = project;
		this.memberName = memberName;
	}
	
	protected RemoveProjectMember() {
	}
	
	public String getProject() {
		return project;
	}
	
	public String getMemberName() {
		return memberName;
	}
	
}
