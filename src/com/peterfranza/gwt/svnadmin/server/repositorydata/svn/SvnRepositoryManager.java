package com.peterfranza.gwt.svnadmin.server.repositorydata.svn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
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
import com.peterfranza.gwt.svnadmin.server.repositorydata.Project;
import com.peterfranza.gwt.svnadmin.server.repositorydata.RepositoryManager;

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
		Session session = sessionProvider.get();
		Transaction tx = session.beginTransaction();
		try {
			session.saveOrUpdate(project);
		} finally {
			tx.commit();
		}
	}

	@Override
	public Project getProjectForName(String name) {
		Session session = sessionProvider.get();
		Transaction tx = session.beginTransaction();
		try {
		return (Project) session.createCriteria(SvnProjectBean.class)
			.add(Restrictions.eq("name", name)).uniqueResult();
		} finally {
			tx.commit();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<Project> getProjects() {
		Session session = sessionProvider.get();
		Transaction tx = session.beginTransaction();
		try {
		return session.createCriteria(SvnProjectBean.class).list();
		} finally {
			tx.commit();
		}
	}

	@Override
	public void scanForProjects() {
		try {
			List<String> paths = getProjectPaths();
			for(String p: paths) {
				if(!isProject(p)) {
					addProject(new SvnProjectBean(p));
				}
			}
			for(Project p: getProjects()) {
				if(!paths.contains(p.getPath())) {
					Session session = sessionProvider.get();
					Transaction tx = session.beginTransaction();
					try {
						session.delete(p);
					} finally {
						tx.commit();
					}
				}
			}
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
	private Collection<? extends String> listEntries( SVNRepository repository, String path ) throws SVNException {
		List<String> s = new ArrayList<String>();
		Collection entries = repository.getDir( path, -1 , null , (Collection) null );
		System.out.println("     searching " + path);
		if(isProject(entries)) {
			s.add("/" + path);
		} else if(isProject(path)) {
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


	private boolean isProject(String path) {
		Session session = sessionProvider.get();
		Transaction tx = session.beginTransaction();
		try {
		return session.createCriteria(SvnProjectBean.class)
			.add(Restrictions.eq("path", path)).list().size() > 0;
		} finally {
			tx.commit();
		}
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
	
//	public static void main(String[] args) {
//		RepositoryManager o = new ServerInjectorFactory().getInjector().getInstance(RepositoryManager.class);
//		o.addProject(new SvnProjectBean("documents"));
//		o.scanForProjects();
//		for(Project p: o.getProjects()) {
//			System.out.println(":: " + p.getName() + "["+p.getPath()+"]");
//		}
//	}
	
}
