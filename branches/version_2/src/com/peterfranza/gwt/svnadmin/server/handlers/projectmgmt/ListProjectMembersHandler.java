package com.peterfranza.gwt.svnadmin.server.handlers.projectmgmt;

import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import com.google.inject.Inject;
import com.peterfranza.gwt.svnadmin.client.actions.projectmanagement.ListProjectMembers;
import com.peterfranza.gwt.svnadmin.client.actions.projectmanagement.ListProjectMembers.MembersList;
import com.peterfranza.gwt.svnadmin.server.entitydata.GroupManager;
import com.peterfranza.gwt.svnadmin.server.repositorydata.RepositoryManager;

public class ListProjectMembersHandler implements ActionHandler<ListProjectMembers, ListProjectMembers.MembersList>{

	private RepositoryManager reposManager;
	private GroupManager groupManager;

	@Inject
	public ListProjectMembersHandler(RepositoryManager reposManager,
			GroupManager groupManager) {
		this.reposManager = reposManager;
		this.groupManager = groupManager;
	}
	
	@Override
	public MembersList execute(ListProjectMembers arg0, ExecutionContext arg1)
			throws ActionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<ListProjectMembers> getActionType() {
		return ListProjectMembers.class;
	}

	@Override
	public void rollback(ListProjectMembers arg0, MembersList arg1,
			ExecutionContext arg2) throws ActionException {}

}
