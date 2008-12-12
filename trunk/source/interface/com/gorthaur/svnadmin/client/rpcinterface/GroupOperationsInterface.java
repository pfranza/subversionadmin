package com.gorthaur.svnadmin.client.rpcinterface;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.gorthaur.svnadmin.client.rpcinterface.beans.Credentials;

@RemoteServiceRelativePath("rpc/group")
public interface GroupOperationsInterface extends RemoteService {

	String createGroup(Credentials requestor, String groupName, List<String> memebers);
	boolean isGroup(Credentials requestor, String groupName);
	List<String> listGroups(Credentials requestor);
	List<String> listGroupMembers(Credentials requestor, String groupName);
	List<String> listGroupNonMembers(Credentials requestor, String groupName);
	
}
