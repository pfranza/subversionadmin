package com.peterfranza.gwt.svnadmin.server.entitydata.local;

import java.util.ArrayList;
import java.util.Collection;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.peterfranza.gwt.svnadmin.server.entitydata.Entity;
import com.peterfranza.gwt.svnadmin.server.entitydata.Group;
import com.peterfranza.gwt.svnadmin.server.entitydata.GroupManager;
import com.peterfranza.gwt.svnadmin.server.entitydata.User;
import com.peterfranza.gwt.svnadmin.server.entitydata.UserManager;
import com.peterfranza.gwt.svnadmin.server.entitydata.cache.CachedUserManager;
import com.peterfranza.gwt.svnadmin.server.repositorydata.ProjectDataWriter;

public class LocalGroupManager implements GroupManager {

	private Provider<Session> sessionProvider;
	private UserManager userManager;
	private ProjectDataWriter dataWriter;


	@Inject
	public LocalGroupManager(
		Provider<Session> sessionProvider,
		UserManager userManager,
		ProjectDataWriter dataWriter) {
		this.sessionProvider = sessionProvider;
		this.dataWriter = dataWriter;
		this.userManager = new CachedUserManager(userManager, 30000);
	}


	@Override
	public void createGroup(final String groupName) {
		if(getGroup(groupName) == null) {
			transact(new TransactionVisitor<Void>() {

				@Override
				public Void transact(Session session) {
					HbmGroupImpl u = new HbmGroupImpl();
					u.setName(groupName);
					session.saveOrUpdate(u);
					return null;
				}			

			});
			dataWriter.saveData();
		} else {
			throw new RuntimeException("Group Exists");
		}
	}

	@Override
	public GroupAdapter getGroup(String groupName) {
		for(GroupAdapter u: getGroups()) {
			if(u.getName().equals(groupName)) {
				return u;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<GroupAdapter> getGroups() {
		return transact(new TransactionVisitor<Collection>() {			
			@Override
			public Collection transact(Session session) {
				Collection<Entity> l = new ArrayList<Entity>();
				Collection<HbmGroupImpl> hgl = session.createCriteria(HbmGroupImpl.class).list();
				for(HbmGroupImpl hg: hgl) {
					if(hg != null && hg.getName() != null) {
						l.add(new GroupAdapter(hg));
					} else if(hg != null){
						session.delete(hg);
					}
				}
				return l; 
			}
		});
	}

	@Override
	public void removeGroup(final String groupName) {
		transact(new TransactionVisitor<Void>() {

			@Override
			public Void transact(Session session) {
				GroupAdapter u = getGroup(groupName);
				if(u != null) {
					session.delete(u.base);
				}
				return null;
			}
		});
		dataWriter.saveData();
	}

	@Override
	public void removeMemberFromGroup(String groupName, final Entity entity) {
		final GroupAdapter g = getGroup(groupName);
		if(g != null) {
			transact(new TransactionVisitor<Void>() {
				@Override
				public Void transact(Session session) {
					g.removeMember(entity);
					session.update(g.base);
					return null;
				}
			});
			dataWriter.saveData();
		} else {
			throw new RuntimeException("Group Not Found");
		}		
	}

	
	@Override
	public void addMemberToGroup(String groupName, final Entity entity) {
		final GroupAdapter g = getGroup(groupName);
		if(g != null) {
			transact(new TransactionVisitor<Void>() {

				@Override
				public Void transact(Session session) {
					g.addMember(entity);
					session.update(g.base);
					return null;
				}
			});
			dataWriter.saveData();
		} else {
			throw new RuntimeException("Group Not Found");
		}
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

	private class GroupAdapter implements Group {
		
		private HbmGroupImpl base;

		public GroupAdapter(HbmGroupImpl base) {
			this.base = base;
		}

		public void addMember(Entity entity) {
			base.addMember(entity);
		}

		public void removeMember(Entity entity) {
			base.removeMember(entity);
		}

		@Override
		public Collection<Entity> getMembers() {
			Collection<Entity> l = new ArrayList<Entity>();
			for(String s: base.getStringMembers()) {
				if(s.startsWith("@")) {
					l.add(getGroup(s.substring(1)));
				} else {
					User user = userManager.getUserForName(s);
					if(user != null) {
						l.add(user);
					}
				}
			}
			return l;
		}

		@Override
		public String getName() {
			return base.getName();
		}
		
	}
	
}
