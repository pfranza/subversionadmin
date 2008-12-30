package com.gorthaur.svnadmin.client.rpcinterface;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gorthaur.svnadmin.client.rpcinterface.beans.Credentials;

public interface AdminOperationsInterfaceAsync {

	void getBackupFilenames(Credentials credentails, AsyncCallback<List<String>> result);
	
}
