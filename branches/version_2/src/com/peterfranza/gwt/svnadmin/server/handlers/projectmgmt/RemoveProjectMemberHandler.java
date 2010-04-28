package com.peterfranza.gwt.svnadmin.server.handlers.projectmgmt;

import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import com.google.inject.Inject;
import com.peterfranza.gwt.svnadmin.client.actions.MessageResult;
import com.peterfranza.gwt.svnadmin.client.actions.projectmanagement.RemoveProjectMember;
import com.peterfranza.gwt.svnadmin.server.entitydata.GroupManager;
import com.peterfranza.gwt.svnadmin.server.repositorydata.RepositoryManager;

public class RemoveProjectMemberHandler implements ActionHandler<RemoveProjectMember, MessageResult>{

	private RepositoryManager reposManager;
	private GroupManager groupManager;

	@Inject
	public RemoveProjectMemberHandler(RepositoryManager reposManager,
			GroupManager groupManager) {
		this.reposManager = reposManager;
		this.groupManager = groupManager;
	}
	
	@Override
	public MessageResult execute(RemoveProjectMember arg0, ExecutionContext arg1)
			throws ActionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<RemoveProjectMember> getActionType() {
		return RemoveProjectMember.class;
	}

	@Override
	public void rollback(RemoveProjectMember arg0, MessageResult arg1,
			ExecutionContext arg2) throws ActionException {}

}
