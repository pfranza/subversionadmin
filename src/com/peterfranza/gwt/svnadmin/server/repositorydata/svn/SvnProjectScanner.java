package com.peterfranza.gwt.svnadmin.server.repositorydata.svn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.io.SVNRepository;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class SvnProjectScanner implements ProjectScanner{

	private Provider<SVNRepository> repositoryProvider;

	@Inject
	public SvnProjectScanner(Provider<SVNRepository> repositoryProvider) {
		this.repositoryProvider = repositoryProvider;
	}
	
	@Override
	public List<String> getProjectPaths(Collection<String> knownPaths) {
		List<String> s = new ArrayList<String>();
		try {
			SVNRepository repository = repositoryProvider.get();
			try {
				s.addAll(listEntries(repository, "", knownPaths));
			} catch (SVNException e) {
				e.printStackTrace();
			} finally {
				if(repository != null) {repository.closeSession();}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return s;
	}
	
	@SuppressWarnings("unchecked")
	private Collection<? extends String> listEntries( SVNRepository repository, String path, Collection<String> knownPaths ) throws SVNException {
		List<String> s = new ArrayList<String>();
		Collection entries = repository.getDir( path, -1 , null , (Collection) null );
		System.out.println("     searching " + path);
		if(isProject(entries)) {
			s.add("/" + path);
		} else if(isProject(path, knownPaths)) {
			s.add("/" + path);
		} else {		
			Iterator iterator = entries.iterator( );
			while ( iterator.hasNext( ) ) {
				SVNDirEntry entry = ( SVNDirEntry ) iterator.next( );	
				if ( entry.getKind() == SVNNodeKind.DIR ) {
					s.addAll(listEntries( repository, ( path.equals( "" ) ) ? entry.getName( ) : path + "/" + entry.getName( ), knownPaths));
				}
			}
		}
		
		return s;
	}
	
	private boolean isProject(String path, Collection<String> knownPaths) {
		for(String kp: knownPaths) {
			if(kp.equals(path)) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	private static boolean isProject(Collection<?> entries) {
		boolean hasTrunk = false;
		boolean hasBranches = false;
		boolean hasTags = false;
		Iterator iterator = entries.iterator( );
		while ( iterator.hasNext( ) ) {
			SVNDirEntry entry = ( SVNDirEntry ) iterator.next( );	
			if ( entry.getKind() == SVNNodeKind.DIR ) {
				if(entry.getName().trim().equalsIgnoreCase("trunk")) {hasTrunk = true;}
				else if(entry.getName().trim().equalsIgnoreCase("branches")) {hasBranches = true;}
				else if(entry.getName().trim().equalsIgnoreCase("tags")) {hasTags = true;}
			}
		}
		return hasTrunk && hasTags && hasBranches;
	}

}
