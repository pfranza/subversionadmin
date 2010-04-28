package com.peterfranza.gwt.svnadmin.server.handlers.groupmgmt;

import java.util.ArrayList;
import java.util.Collection;

import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import com.google.inject.Inject;
import com.peterfranza.gwt.svnadmin.client.actions.groupmanagement.ListGroups;
import com.peterfranza.gwt.svnadmin.client.actions.groupmanagement.ListGroups.GroupsList;
import com.peterfranza.gwt.svnadmin.server.entitydata.Group;
import com.peterfranza.gwt.svnadmin.server.entitydata.GroupManager;

public class ListGroupHandler implements ActionHandler<ListGroups, ListGroups.GroupsList>{

	private GroupManager groupManager;

	@Inject
	public ListGroupHandler(GroupManager groupManager) {
		this.groupManager = groupManager;
	}
	
	@Override
	public GroupsList execute(ListGroups arg0, ExecutionContext arg1)
			throws ActionException {
		return new GroupsList(asStrings(groupManager.getGroups()));
	}

	private ArrayList<String> asStrings(Collection<? extends Group> groups) {
		ArrayList<String> l = new ArrayList<String>();
		for(Group u: groups) {
			l.add(u.getName());
		}
		return l;
	}

	@Override
	public Class<ListGroups> getActionType() {
		return ListGroups.class;
	}

	@Override
	public void rollback(ListGroups arg0, GroupsList arg1, ExecutionContext arg2)
			throws ActionException {}

}
