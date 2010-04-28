package com.peterfranza.gwt.svnadmin.client.actions.projectmanagement;

import net.customware.gwt.dispatch.shared.Action;

import com.peterfranza.gwt.svnadmin.client.actions.MessageResult;

public class ProjectSetMemberAccess implements Action<MessageResult>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2302667685733712891L;
	
	private String project;
	private String username;
	private boolean canRead;
	private boolean canWrite;
	
	public ProjectSetMemberAccess(String project, String username,
			boolean canRead, boolean canWrite) {
		super();
		this.project = project;
		this.username = username;
		this.canRead = canRead;
		this.canWrite = canWrite;
	}
	
	public ProjectSetMemberAccess() {
	}
	
	public String getProject() {
		return project;
	}
	
	public String getUsername() {
		return username;
	}
	
	public boolean isCanRead() {
		return canRead || isCanWrite();
	}
	
	public boolean isCanWrite() {
		return canWrite;
	}
}
