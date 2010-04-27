package com.peterfranza.gwt.svnadmin.server.entitydata.local;

import java.util.Collection;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.peterfranza.gwt.svnadmin.server.entitydata.User;
import com.peterfranza.gwt.svnadmin.server.entitydata.UserManager;
import com.peterfranza.gwt.svnadmin.server.util.ConfigFileWriter;
import com.peterfranza.gwt.svnadmin.server.util.Crypt;

public class LocalUserManager implements UserManager{

	private Crypt crypt;
	private Provider<Session> sessionProvider;
	private ConfigFileWriter passwordFileWriter;


	@Inject
	public LocalUserManager(Crypt crypt,
			Provider<Session> sessionProvider,
			@Named("passwordFile") ConfigFileWriter passwordFileWriter) {
		this.crypt = crypt;
		this.sessionProvider = sessionProvider;
		this.passwordFileWriter = passwordFileWriter;
	}
	
	@Override
	public HbmUserImpl getUserForName(String username) {
		for(HbmUserImpl u: getUsers()) {
			if(u.getName().equals(username)) {
				return u;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<HbmUserImpl> getUsers() {
		return transact(new TransactionVisitor<Collection>() {
			
			@Override
			public Collection transact(Session session) {
				return session.createCriteria(HbmUserImpl.class).list();
			}
	
		});
	}
	
	@Override
	public boolean authenticate(String username, String password) {
		User u = getUserForName(username);
		return u != null ? crypt.matches(u.getPassword(), password) : false;
	}
	
	@Override
	public void removeUser(final String username) {
		transact(new TransactionVisitor<Void>() {

			@Override
			public Void transact(Session session) {
				User u = getUserForName(username);
				if(u != null) {
					session.delete(u);
				}
				return null;
			}
		});
		exportPasswordFile();
	}
	
	@Override
	public void createUser(final String username) {

		if(getUserForName(username) == null) {
			transact(new TransactionVisitor<Void>() {

				@Override
				public Void transact(Session session) {
					HbmUserImpl u = new HbmUserImpl();
					u.setName(username);
					session.saveOrUpdate(u);
					return null;
				}			

			});
			exportPasswordFile();
		} else {
			throw new RuntimeException("User Exists");
		}
	}
	
	@Override
	public void setPassword(String username, final String password) {
		mutateUser(username, new UserVisitor() {			
			@Override
			public void modifyUser(HbmUserImpl user) {
				user.setPassword(crypt.crypt(password));
			}
		});
	}
	
	@Override
	public void setEmailAddress(String username, final String email) {
		mutateUser(username, new UserVisitor() {			
			@Override
			public void modifyUser(HbmUserImpl user) {
				user.setEmailAddress(email);
			}
		});
	}
	
	@Override
	public void setAdministrator(String username, final boolean isAdmin) {
		mutateUser(username, new UserVisitor() {			
			@Override
			public void modifyUser(HbmUserImpl user) {
				user.setAdministrator(isAdmin);
			}
		});
	}
	
	@Override
	public boolean isMutable() {
		return true;
	}
	
	private void exportPasswordFile() {
		StringBuffer buf = new StringBuffer();
		for(User u: getUsers()) {
			buf.append(u.getName()).append(":").append(u.getPassword()).append(System.getProperty("line.separator"));
		}
		passwordFileWriter.save(buf.toString().trim());
	}
	
	private void mutateUser(final String username, final UserVisitor visitor) {
		transact(new TransactionVisitor<Void>() {

			@Override
			public Void transact(Session session) {
				HbmUserImpl u = getUserForName(username);
				if(u != null) {
					visitor.modifyUser(u);
					session.update(u);
				} else {
					throw new RuntimeException("User not found");
				}
				return null;
			}
		});
		exportPasswordFile();
	}
	
	private interface UserVisitor {
		void modifyUser(HbmUserImpl user);
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
