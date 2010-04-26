package com.peterfranza.gwt.svnadmin.server.repositorydata.svn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.tmatesoft.svn.core.SVNException;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.peterfranza.gwt.svnadmin.server.entitydata.Entity;
import com.peterfranza.gwt.svnadmin.server.repositorydata.Project;
import com.peterfranza.gwt.svnadmin.server.repositorydata.RepositoryManager;

public class SvnRepositoryManager implements RepositoryManager {

	private Provider<Session> sessionProvider;
	private ProjectScanner scanner;

	@Inject
	public SvnRepositoryManager(
			ProjectScanner scanner,
			Provider<Session> sessionProvider) throws SVNException {
		this.sessionProvider = sessionProvider;
		this.scanner = scanner;
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
			List<String> paths = scanner.getProjectPaths(asString(getProjects()));
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



	private List<String> asString(Collection<Project> projects) {
		ArrayList<String> list = new ArrayList<String>();
		for(Project p: projects) {
			list.add(p.getPath());
		}
		return list;
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


	@Override
	public boolean canRead(String name, Entity entity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canWrite(String name, Entity entity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSubscribed(String name, Entity entity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setReadWrite(String name, Entity entity, ACCESS access) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void subscribe(String name, Entity entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unsubscribe(String name, Entity entity) {
		// TODO Auto-generated method stub
		
	}
	
}
