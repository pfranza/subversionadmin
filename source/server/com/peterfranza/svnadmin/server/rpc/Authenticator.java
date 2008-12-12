package com.peterfranza.svnadmin.server.rpc;

import org.mortbay.gwt.AsyncRemoteServiceServlet;

import com.gorthaur.svnadmin.client.rpcinterface.AuthenticationInterface;
import com.peterfranza.svnadmin.server.acldb.ACLOperationsDelegate;

public class Authenticator extends AsyncRemoteServiceServlet  implements AuthenticationInterface {

	private static final long serialVersionUID = -8920303489482728570L;

	public boolean authenticate(String username, String password) {
		return ACLOperationsDelegate.getInstance().getUserOperations().authenticate(username, password);
	}

	public boolean isAdmin(String username) {
		return ACLOperationsDelegate.getInstance().getUserOperations().isAdmin(username);
	}
	
}
