package com.peterfranza.gwt.svnadmin.server.repositorydata;

import com.google.inject.ImplementedBy;
import com.peterfranza.gwt.svnadmin.server.repositorydata.svn.DefaultChangeSetMiner;

@ImplementedBy(DefaultChangeSetMiner.class)
public interface ChangeSetMiner {

	public ChangeSet getSummary(long revisionNumber);

}