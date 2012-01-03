package com.peterfranza.gwt.svnadmin.server.repositorydata.svn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.NonUniqueResultException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.tmatesoft.svn.core.SVNException;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.peterfranza.gwt.svnadmin.server.entitydata.Entity;
import com.peterfranza.gwt.svnadmin.server.entitydata.Group;
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
		for(Project p: getProjects()) {
			System.out.println("  -- " + p.getPath());
		}
	}

	@Override
	public SvnProjectBean getProjectForName(final String name) {
		
		return transact(new TransactionVisitor<SvnProjectBean>() {
			@Override
			public SvnProjectBean transact(Session session) {
				try {
					return (SvnProjectBean) session.createCriteria(SvnProjectBean.class)
							.add(Restrictions.eq("path", name)).uniqueResult();
				} catch (NonUniqueResultException e) {
					List<?> lst = session.createCriteria(SvnProjectBean.class)
							.add(Restrictions.eq("path", name)).list();
					for(int i = 1; i < lst.size(); i++) {
						session.delete(lst.get(i));
					}

					return (SvnProjectBean) lst.get(0);
				}
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
						System.out.println("rm: " + p.getPath());
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
		return getProjectForName(name).canRead(asEntityString(entity));
	}

	@Override
	public boolean canWrite(String name, Entity entity) {
		return getProjectForName(name).canWrite(asEntityString(entity));
	}

	@Override
	public void setReadWrite(String name, final Entity entity, final ACCESS access) {
		mutateProject(name, new ProjectVisitor() {			
			@Override
			public void modifyProject(SvnProjectBean project) {
				project.setAccess(asEntityString(entity), access);
			}
		});
	}

	@Override
	public void subscribe(String name, final User entity) {
		mutateProject(name, new ProjectVisitor() {		
			@Override
			public void modifyProject(SvnProjectBean project) {
				project.subscribe(asEntityString(entity));
			}
		});
	}

	@Override
	public void unsubscribe(String name, final User entity) {
		mutateProject(name, new ProjectVisitor() {		
			@Override
			public void modifyProject(SvnProjectBean project) {
				project.unsubscribe(asEntityString(entity));
			}
		});
	}
	
	@Override
	public boolean isSubscribed(String name, User entity) {
		return getProjectForName(name).isSubscribed(asEntityString(entity));
	}
	
	private String asEntityString(Entity entity) {
		if(entity instanceof Group) {
			return "@" + entity.getName();
		}
		return entity.getName();
	}
	
	private void mutateProject(final String projectPath, final ProjectVisitor visitor) {
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
	
	private interface ProjectVisitor {
		void modifyProject(SvnProjectBean project);
	}
	
	private <T> T transact(TransactionVisitor<T> visitor) {
		Session s = sessionProvider.get();
		Transaction tx = s.beginTransaction();
		T value = null;
		try {
			value = visitor.transact(s);
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			throw new RuntimeException(e);
		} finally {
			s.close();	
		}
		return value;	
	}
	
	private interface TransactionVisitor<T> {
		 T transact(Session session);
	}
	
}
