package com.gorthaur.svnadmin.client.rpcinterface;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.gorthaur.svnadmin.client.rpcinterface.beans.Credentials;

@RemoteServiceRelativePath("rpc/admin")
public interface AdminOperationsInterface extends RemoteService {

	List<String> getBackupFilenames(Credentials credentails);

}
