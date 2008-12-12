package com.peterfranza.svnadmin.server.rpc;

import org.mortbay.gwt.AsyncRemoteServiceServlet;

import com.gorthaur.svnadmin.client.rpcinterface.UserOperationsInterface;
import com.gorthaur.svnadmin.client.rpcinterface.beans.Credentials;
import com.peterfranza.svnadmin.server.acldb.ACLOperationsDelegate;

public class UserOperations extends AsyncRemoteServiceServlet implements UserOperationsInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7101937577981436861L;

	public String createNewUser(Credentials requestor, String username,
			String password, String email) {
		if(ACLOperationsDelegate.getInstance().getUserOperations().isAdmin(requestor.getUsername())) {
			ACLOperationsDelegate.getInstance().getUserOperations()
				.addNewUser(username, password, email);
			return "ok";
		} else {
			return "Insufficiant Access";
		}
		
	}

	public boolean isUser(Credentials requestor, String username) {
		return ACLOperationsDelegate.getInstance().getUserOperations().getUser(username) != null;
	}

}
