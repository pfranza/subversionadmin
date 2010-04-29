package com.peterfranza.gwt.svnadmin.server.handlers.groupmgmt;

import java.util.ArrayList;
import java.util.Collection;

import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import com.google.inject.Inject;
import com.peterfranza.gwt.svnadmin.client.actions.groupmanagement.ListUsersInGroup;
import com.peterfranza.gwt.svnadmin.client.actions.usermanagement.ListUsers.UserList;
import com.peterfranza.gwt.svnadmin.server.entitydata.Entity;
import com.peterfranza.gwt.svnadmin.server.entitydata.GroupManager;

public class ListUsersInGroupHandler implements ActionHandler<ListUsersInGroup, UserList>{

	private GroupManager groupManager;

	@Inject
	public ListUsersInGroupHandler(GroupManager groupManager) {
		this.groupManager = groupManager;
	}
	
	@Override
	public UserList execute(ListUsersInGroup arg0, ExecutionContext arg1)
			throws ActionException {
		return new UserList(asStrings(groupManager.getGroup(arg0.getGroupName()).getMembers()));
	}

	private ArrayList<String> asStrings(Collection<Entity> members) {
		ArrayList<String> l = new ArrayList<String>();
		for(Entity u: members) {
			if(u != null) {
				l.add(u.getName());
			}
		}
		return l;
	}

	@Override
	public Class<ListUsersInGroup> getActionType() {
		return ListUsersInGroup.class;
	}

	@Override
	public void rollback(ListUsersInGroup arg0, UserList arg1,
			ExecutionContext arg2) throws ActionException {}

}
