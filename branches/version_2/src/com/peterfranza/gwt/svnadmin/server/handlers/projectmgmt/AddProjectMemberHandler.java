package com.peterfranza.gwt.svnadmin.server.handlers.projectmgmt;

import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import com.google.inject.Inject;
import com.peterfranza.gwt.svnadmin.client.actions.MessageResult;
import com.peterfranza.gwt.svnadmin.client.actions.projectmanagement.AddProjectMember;
import com.peterfranza.gwt.svnadmin.server.entitydata.GroupManager;
import com.peterfranza.gwt.svnadmin.server.repositorydata.RepositoryManager;
import com.peterfranza.gwt.svnadmin.server.repositorydata.RepositoryManager.ACCESS;

public class AddProjectMemberHandler implements ActionHandler<AddProjectMember, MessageResult>{

	private RepositoryManager reposManager;
	private GroupManager groupManager;

	@Inject
	public AddProjectMemberHandler(RepositoryManager reposManager,
			GroupManager groupManager) {
		this.reposManager = reposManager;
		this.groupManager = groupManager;
	}
	
	@Override
	public MessageResult execute(AddProjectMember arg0, ExecutionContext arg1)
			throws ActionException {
		reposManager.setReadWrite(arg0.getProject(), 
				groupManager.getGroup(arg0.getMemberName()),
				ACCESS.READ);
		return new MessageResult("", "Access Granted");
	}

	@Override
	public Class<AddProjectMember> getActionType() {
		return AddProjectMember.class;
	}

	@Override
	public void rollback(AddProjectMember arg0, MessageResult arg1,
			ExecutionContext arg2) throws ActionException {}

}
