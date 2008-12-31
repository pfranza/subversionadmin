package com.gorthaur.svnadmin.client.rpcinterface;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("rpc/auth")
public interface AuthenticationInterface extends RemoteService {

	boolean authenticate(String username, String password);
	boolean isAdmin(String username);
	
	boolean authenticateBasic(String username, String password);
	String changePasswordBasic(String username, String oldPassword, String newPassword);
	
}
