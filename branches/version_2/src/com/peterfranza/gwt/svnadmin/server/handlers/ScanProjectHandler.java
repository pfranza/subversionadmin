package com.peterfranza.gwt.svnadmin.server.handlers;

import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import com.google.inject.Inject;
import com.peterfranza.gwt.svnadmin.client.actions.MessageResult;
import com.peterfranza.gwt.svnadmin.client.actions.projectmanagement.ScanProjectsRequest;
import com.peterfranza.gwt.svnadmin.server.repositorydata.RepositoryManager;

public class ScanProjectHandler implements ActionHandler<ScanProjectsRequest, MessageResult>{

	private RepositoryManager reposManager;

	@Inject
	public ScanProjectHandler(RepositoryManager reposManager) {
		this.reposManager = reposManager;
	}
	
	@Override
	public MessageResult execute(ScanProjectsRequest arg0, ExecutionContext arg1)
			throws ActionException {
		reposManager.scanForProjects();
		return new MessageResult("", "Scan Finished");
	}

	@Override
	public Class<ScanProjectsRequest> getActionType() {
		return ScanProjectsRequest.class;
	}

	@Override
	public void rollback(ScanProjectsRequest arg0, MessageResult arg1,
			ExecutionContext arg2) throws ActionException {}

}
