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

	void deleteUser(Credentials requestor, String username, AsyncCallback<Void> response);
	
	void updateEmailAddress(Credentials requestor, String username, String newEmail, AsyncCallback<Void> response);
	void updatePassword(Credentials requestor, String username, String newPassword, AsyncCallback<Void> response);
	void updateIsAdmin(Credentials requestor, String username, boolean isAdmin, AsyncCallback<Void> response);
	
	void getSubscriptions(Credentials requestor, String username, AsyncCallback<List<String>> response);
	void getAllProjects(Credentials requestor, AsyncCallback<List<String>> response);

	void joinGroup(Credentials credentials, String username, String groupName, AsyncCallback<Void> response);
	void leaveGroup(Credentials credentials, String username, String groupName, AsyncCallback<Void> response);

	void addSubscription(Credentials credentials, String value, String s,
			AsyncCallback<Void> voidCallback);

	void removeSubscription(Credentials credentials, String value, String r,
			AsyncCallback<Void> voidCallback);
}
