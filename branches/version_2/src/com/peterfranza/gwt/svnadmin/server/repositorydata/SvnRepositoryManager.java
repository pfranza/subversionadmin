package com.peterfranza.gwt.svnadmin.server.repositorydata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.peterfranza.gwt.svnadmin.server.ServerInjectorFactory;

public class SvnRepositoryManager implements RepositoryManager {

	private String url;
	private String username;
	private String password;
	private Provider<Session> sessionProvider;

	@Inject
	public SvnRepositoryManager(
			@Named("repositoryUrl") String url,
			@Named("repositoryUsername") String username,
			@Named("repositoryPassword") String password,
			Provider<Session> sessionProvider) throws SVNException {
		this.url = url;
		this.username = username;
		this.password = password;
		
		this.sessionProvider = sessionProvider;
		
		if(url.startsWith("file")) {
			FSRepositoryFactory.setup();
		} else if(url.startsWith("http")) {
			DAVRepositoryFactory.setup();
		} else if(url.startsWith("svn")) {
			SVNRepositoryFactoryImpl.setup();
		} else {
			throw new RuntimeException("Can't setup svn connector");
		}
	}
	
	@Override
	public void addProject(Project project) {
		// TODO Auto-generated method stub

	}

	@Override
	public Project getProjectForName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Project> getProjects() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void scanForProjects() {
		try {
			List<String> paths = getProjectPaths();
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	private SVNRepository getRepository() throws SVNException {		
		SVNRepository repository = SVNRepositoryFactory.create(
				SVNURL.parseURIEncoded(url));

		ISVNAuthenticationManager authManager = SVNWCUtil
				.createDefaultAuthenticationManager(username, password);

		repository.setAuthenticationManager(authManager);
		return repository;
	}
	
	private List<String> getProjectPaths() throws SVNException {
		List<String> s = new ArrayList<String>();
		SVNRepository repository = getRepository();
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
		System.out.println("     searching " + path);
		if(isProject(entries)) {
			s.add("/" + path);
		} else if(isManualProject(path)) {
			s.add("/" + path);
		} else {		
			Iterator iterator = entries.iterator( );
			while ( iterator.hasNext( ) ) {
				SVNDirEntry entry = ( SVNDirEntry ) iterator.next( );	
				if ( entry.getKind() == SVNNodeKind.DIR ) {
					s.addAll(listEntries( repository, ( path.equals( "" ) ) ? entry.getName( ) : path + "/" + entry.getName( ) ));
				}
			}
		}
		
		return s;
	}


	private static boolean isManualProject(String path) {
		// TODO Auto-generated method stub
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
	
	public static void main(String[] args) {
		SvnRepositoryManager o = new ServerInjectorFactory().getInjector().getInstance(SvnRepositoryManager.class);
		o.scanForProjects();
	}
	
}
