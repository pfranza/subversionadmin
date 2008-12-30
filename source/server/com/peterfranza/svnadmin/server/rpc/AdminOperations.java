package com.peterfranza.svnadmin.server.rpc;

import java.util.List;

import org.mortbay.gwt.AsyncRemoteServiceServlet;

import com.gorthaur.svnadmin.client.rpcinterface.AdminOperationsInterface;
import com.gorthaur.svnadmin.client.rpcinterface.beans.Credentials;
import com.peterfranza.svnadmin.server.BackupListings;

public class AdminOperations extends AsyncRemoteServiceServlet implements AdminOperationsInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7655065680054957146L;

	public List<String> getBackupFilenames(Credentials credentails) {
		return BackupListings.getFileNames();
	}

}
