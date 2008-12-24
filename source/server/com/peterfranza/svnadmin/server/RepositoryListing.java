package com.peterfranza.svnadmin.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.io.SVNRepository;

public class RepositoryListing {

	public static List<String> getProjectPaths(String repos, String username,
			String password) {
		List<String> s = new ArrayList<String>();
		SVNRepository repository = SVNAgent.getRepository(repos, username, password);
		try {
			s.addAll(listEntries(repository, ""));
		} catch (SVNException e) {
			e.printStackTrace();
		} finally {
			if(repository != null) {repository.closeSession();}
		}
		
		return s;
	}
	
	@SuppressWarnings("unchecked")
	private static Collection<? extends String> listEntries( SVNRepository repository, String path ) throws SVNException {
		List<String> s = new ArrayList<String>();
		Collection entries = repository.getDir( path, -1 , null , (Collection) null );
		
		if(isProject(entries)) {
			s.add(path);
		}
		
		Iterator iterator = entries.iterator( );
		while ( iterator.hasNext( ) ) {
			SVNDirEntry entry = ( SVNDirEntry ) iterator.next( );	
			if ( entry.getKind() == SVNNodeKind.DIR ) {
				s.addAll(listEntries( repository, ( path.equals( "" ) ) ? entry.getName( ) : path + "/" + entry.getName( ) ));
			}
		}
		return s;
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
