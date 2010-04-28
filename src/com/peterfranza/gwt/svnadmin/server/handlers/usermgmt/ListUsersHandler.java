package com.peterfranza.gwt.svnadmin.server.handlers.usermgmt;

import java.util.ArrayList;
import java.util.Collection;

import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import com.google.inject.Inject;
import com.peterfranza.gwt.svnadmin.client.actions.usermanagement.ListUsers;
import com.peterfranza.gwt.svnadmin.client.actions.usermanagement.ListUsers.UserList;
import com.peterfranza.gwt.svnadmin.server.entitydata.User;
import com.peterfranza.gwt.svnadmin.server.entitydata.UserManager;

public class ListUsersHandler implements ActionHandler<ListUsers, UserList>{

	private UserManager userManager;

	@Inject
	public ListUsersHandler(UserManager userManager) {
		this.userManager = userManager;
	}
	
	@Override
	public UserList execute(ListUsers arg0, ExecutionContext arg1)
			throws ActionException {
		return new UserList(asNamesList(userManager.getUsers()));
	}

	private ArrayList<String> asNamesList(Collection<? extends User> users) {
		ArrayList<String> l = new ArrayList<String>();
		for(User u: users) {
			l.add(u.getName());
		}
		System.out.println("Listed " + l.size());
		return l;
	}

	@Override
	public Class<ListUsers> getActionType() {
		return ListUsers.class;
	}

	@Override
	public void rollback(ListUsers arg0, UserList arg1, ExecutionContext arg2)
			throws ActionException {}

}
