package com.peterfranza.gwt.svnadmin.server.repositorydata;

import java.util.Date;
import java.util.List;

public interface ChangeSet {

	public abstract long getRevision();

	public abstract String getAuthor();

	public abstract String getMessage();

	public abstract String getChanges();

	public abstract Date getDate();

	public abstract List<String> getChangeSet();

}