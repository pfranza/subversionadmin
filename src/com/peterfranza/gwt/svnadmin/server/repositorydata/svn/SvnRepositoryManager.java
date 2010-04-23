package com.peterfranza.gwt.svnadmin.server.repositorydata.svn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;
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
import com.peterfranza.gwt.svnadmin.server.repositorydata.ChangeSet;
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
	
	public ChangeSet getSummary(long revisionNumber) {

		long startRevision = revisionNumber;
		long endRevision = revisionNumber;

		SVNRepository repository = null;
		try {
			repository = getRepository();
			Collection<?> logEntries = repository.log(new String[] { "" },
					null, startRevision, endRevision, true, true);

			for (Iterator<?> entries = logEntries.iterator(); entries.hasNext();) {
				SVNLogEntry logEntry = (SVNLogEntry) entries.next();
				return new ChangeSummaryImpl(logEntry);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(repository != null) { repository.closeSession(); }
		}
		return null;
	}

	public static class ChangeSummaryImpl implements ChangeSet {

		private long revision;
		private String author;
		private String message;
		private String changes;
		private Date date;
		private List<String> changesList = new ArrayList<String>();

		private ChangeSummaryImpl(SVNLogEntry logEntry) {
			this.revision = logEntry.getRevision();
			this.author = logEntry.getAuthor();
			this.message = logEntry.getMessage();
			this.date = logEntry.getDate();

			Set<?> changedPathsSet = logEntry.getChangedPaths().keySet();
			StringBuffer buf = new StringBuffer();
			for (Iterator<?> changedPaths = changedPathsSet.iterator(); changedPaths
					.hasNext();) {
				SVNLogEntryPath entryPath = (SVNLogEntryPath) logEntry
						.getChangedPaths().get(changedPaths.next());
				buf
						.append(
								" "
										+ entryPath.getType()
										+ " "
										+ entryPath.getPath()
										+ ((entryPath.getCopyPath() != null) ? " (from "
												+ entryPath.getCopyPath()
												+ " revision "
												+ entryPath.getCopyRevision()
												+ ")"
												: "")).append(
								System.getProperty("line.separator"));
				changesList.add(entryPath.getPath());
				if (entryPath.getCopyPath() != null) {
					changesList.add(entryPath.getCopyPath());
				}
			}

			this.changes = buf.toString();
		}

		public final long getRevision() {
			return revision;
		}

		public final String getAuthor() {
			return author;
		}

		public final String getMessage() {
			return message;
		}

		public final String getChanges() {
			return changes;
		}

		public final Date getDate() {
			return date;
		}

		@Override
		public String toString() {
			StringBuffer buf = new StringBuffer();
			
			buf.append(getMessage())
				.append(System.getProperty("line.separator"))
				.append(System.getProperty("line.separator"))
				.append(System.getProperty("line.separator"));
			
			buf.append("revision: ").append(getRevision()).append(
					System.getProperty("line.separator"));
			buf.append("author: ").append(getAuthor()).append(
					System.getProperty("line.separator"));
			buf.append("date: ").append(getDate()).append(
					System.getProperty("line.separator"));

			buf.append("changes: ")
					.append(System.getProperty("line.separator"));
			buf.append(getChanges()).append(
					System.getProperty("line.separator"));
			return buf.toString().trim();
		}

		public List<String> getChangeSet() {
			return changesList;
		}

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
