package com.peterfranza.gwt.svnadmin.server.repositorydata.svn;

import java.util.Collection;
import java.util.List;

import com.google.inject.ImplementedBy;

@ImplementedBy(SvnProjectScanner.class)
public interface ProjectScanner {

	List<String> getProjectPaths(Collection<String> knownPaths);
	
}
