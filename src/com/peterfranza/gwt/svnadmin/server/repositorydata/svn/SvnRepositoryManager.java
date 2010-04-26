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
import com.peterfranza.gwt.svnadmin.server.entitydata.User;
import com.peterfranza.gwt.svnadmin.server.repositorydata.Project;
import com.peterfranza.gwt.svnadmin.server.repositorydata.ProjectDataWriter;
import com.peterfranza.gwt.svnadmin.server.repositorydata.RepositoryManager;

public class SvnRepositoryManager implements RepositoryManager {

	private Provider<Session> sessionProvider;
	private ProjectScanner scanner;
	private ProjectDataWriter dataWriter;

	@Inject
	public SvnRepositoryManager(
			ProjectScanner scanner,
			Provider<Session> sessionProvider,
			ProjectDataWriter dataWriter) throws SVNException {
		this.sessionProvider = sessionProvider;
		this.scanner = scanner;
		this.dataWriter = dataWriter;
	}
	
	@Override
	public void addProject(final String projectName) {
		transact(new TransactionVisitor<Void>() {
			@Override
			public Void transact(Session session) {
				session.saveOrUpdate(new SvnProjectBean(projectName));
				return null;
			}
		});
		dataWriter.saveData();
	}

	@Override
	public SvnProjectBean getProjectForName(final String name) {
		return transact(new TransactionVisitor<SvnProjectBean>() {
			@Override
			public SvnProjectBean transact(Session session) {
				return (SvnProjectBean) session.createCriteria(SvnProjectBean.class)
					.add(Restrictions.eq("path", name)).uniqueResult();
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<Project> getProjects() {
		return transact(new TransactionVisitor<Collection<Project>>() {
			@Override
			public Collection<Project> transact(Session session) {
				return session.createCriteria(SvnProjectBean.class).list();
			}
		});
	}

	@Override
	public void scanForProjects() {
		List<String> paths = scanner.getProjectPaths(asString(getProjects()));
		for(String p: paths) {
			if(!isProject(p)) {
				addProject(p);
			}
		}
		for(final Project p: getProjects()) {
			if(!paths.contains(p.getPath())) {
				transact(new TransactionVisitor<Void>() {
					@Override
					public Void transact(Session session) {
						session.delete(p);
						return null;
					}
				});
			}
		}
		dataWriter.saveData();
	}

	private List<String> asString(Collection<Project> projects) {
		ArrayList<String> list = new ArrayList<String>();
		for(Project p: projects) {
			list.add(p.getPath());
		}
		return list;
	}

	private boolean isProject(final String path) {
		return transact(new TransactionVisitor<Boolean>() {
			@Override
			public Boolean transact(Session session) {
				return session.createCriteria(SvnProjectBean.class)
					.add(Restrictions.eq("path", path)).list().size() > 0;
			}
		});
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
	public boolean isSubscribed(String name, User entity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setReadWrite(String name, Entity entity, ACCESS access) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void subscribe(String name, User entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unsubscribe(String name, User entity) {
		// TODO Auto-generated method stub
		
	}
	
	private void mutateProject(final String projectPath, final UserVisitor visitor) {
		transact(new TransactionVisitor<Void>() {

			@Override
			public Void transact(Session session) {
				SvnProjectBean u = getProjectForName(projectPath);
				if(u != null) {
					visitor.modifyProject(u);
					session.update(u);
				} else {
					throw new RuntimeException("Project not found");
				}
				return null;
			}
		});
		dataWriter.saveData();
	}
	
	private interface UserVisitor {
		void modifyProject(SvnProjectBean project);
	}
	
	private <T> T transact(TransactionVisitor<T> visitor) {
		Session s = sessionProvider.get();
		Transaction tx = s.beginTransaction();
		T value = null;
		value = visitor.transact(s);
		tx.commit();
		s.close();
		return value;		
	}
	
	private interface TransactionVisitor<T> {
		 T transact(Session session);
	}
	
}
