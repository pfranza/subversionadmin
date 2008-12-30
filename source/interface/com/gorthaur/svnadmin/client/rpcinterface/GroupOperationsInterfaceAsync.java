package com.gorthaur.svnadmin.client.rpcinterface;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gorthaur.svnadmin.client.rpcinterface.beans.Credentials;

public interface GroupOperationsInterfaceAsync {

	void createGroup(Credentials requestor, String groupName, List<String> memebers, AsyncCallback<String> result);
	void isGroup(Credentials requestor, String groupName, AsyncCallback<Boolean> result);
	void listGroups(Credentials requestor, AsyncCallback<List<String>> result);
	
	void listGroupMembers(Credentials requestor, String groupName, AsyncCallback<List<String>> result);
	void listGroupNonMembers(Credentials requestor, String groupName, AsyncCallback<List<String>> result);
	
	void deleteGroup(Credentials requestor, String groupName, AsyncCallback<List<Void>> result);
	void addMember(Credentials credentials, String value, String string,
			AsyncCallback<List<Void>> asyncCallback);
	void removeMember(Credentials credentials, String value, String string,
			AsyncCallback<List<Void>> asyncCallback);
}
