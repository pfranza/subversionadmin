package com.gorthaur.svnadmin.client.rpcinterface;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gorthaur.svnadmin.client.rpcinterface.beans.Credentials;
import com.gorthaur.svnadmin.client.rpcinterface.beans.UserInfo;

public interface UserOperationsInterfaceAsync {

	void createNewUser(Credentials requestor, String username, String password, 
			String email, AsyncCallback<String> result);

	void isUser(Credentials requestor, String username, AsyncCallback<Boolean> result);
	
	void getAllUsers(Credentials requestor, AsyncCallback<List<UserInfo>> response);
	
	void updateUser(Credentials requestor, UserInfo info, AsyncCallback<Void> response);
	
	void deleteUser(Credentials requestor, String username, AsyncCallback<Void> response);
}
