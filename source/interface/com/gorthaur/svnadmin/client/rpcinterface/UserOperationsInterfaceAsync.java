package com.gorthaur.svnadmin.client.rpcinterface;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gorthaur.svnadmin.client.rpcinterface.beans.Credentials;

public interface UserOperationsInterfaceAsync {

	void createNewUser(Credentials requestor, String username, String password, 
			String email, AsyncCallback<String> result);

	void isUser(Credentials requestor, String username, AsyncCallback<Boolean> result);
}
