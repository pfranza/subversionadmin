package com.gorthaur.svnadmin.client.rpcinterface;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.gorthaur.svnadmin.client.rpcinterface.beans.Credentials;

@RemoteServiceRelativePath("rpc/user")
public interface UserOperationsInterface extends RemoteService {

	String createNewUser(Credentials requestor, String username, String password, String email);
	boolean isUser(Credentials requestor, String username);
}
