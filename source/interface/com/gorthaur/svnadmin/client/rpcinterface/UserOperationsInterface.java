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
	void deleteUser(Credentials requestor, String username);
	
	void updateEmailAddress(Credentials requestor, String username, String newEmail);
	void updatePassword(Credentials requestor, String username, String newPassword);
	void updateIsAdmin(Credentials requestor, String username, boolean isAdmin);
	
	List<String> getSubscriptions(Credentials requestor, String username);
	List<String> getAllProjects(Credentials requestor);
	

	void joinGroup(Credentials credentials, String username, String groupName);
	void leaveGroup(Credentials credentials, String username, String groupName);

	void addSubscription(Credentials credentials, String value, String s);

	void removeSubscription(Credentials credentials, String value, String r);
	
}
