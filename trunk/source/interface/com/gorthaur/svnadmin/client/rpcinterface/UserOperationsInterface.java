package com.gorthaur.svnadmin.client.rpcinterface;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.gorthaur.svnadmin.client.rpcinterface.beans.Credentials;
import com.gorthaur.svnadmin.client.rpcinterface.beans.UserInfo;

@RemoteServiceRelativePath("rpc/user")
public interface UserOperationsInterface extends RemoteService {

	String createNewUser(Credentials requestor, String username, String password, String email);
	boolean isUser(Credentials requestor, String username);
	List<UserInfo> getAllUsers(Credentials requestor);
	void updateUser(Credentials requestor, UserInfo info);
	void deleteUser(Credentials requestor, String username);
}
