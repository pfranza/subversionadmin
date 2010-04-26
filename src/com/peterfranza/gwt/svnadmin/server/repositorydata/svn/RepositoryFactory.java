package com.peterfranza.gwt.svnadmin.server.repositorydata.svn;

import org.tmatesoft.svn.core.SVNException;
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

public class RepositoryFactory implements Provider<SVNRepository> {

	private String url;
	private String username;
	private String password;

	@Inject
	public RepositoryFactory(
			@Named("repositoryUrl") String url,
			@Named("repositoryUsername") String username,
			@Named("repositoryPassword") String password) throws SVNException {
		this.url = url;
		this.username = username;
		this.password = password;
		
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
	public SVNRepository get() {
		try {
			SVNRepository repository = SVNRepositoryFactory.create(
					SVNURL.parseURIEncoded(url));

			ISVNAuthenticationManager authManager = SVNWCUtil
			.createDefaultAuthenticationManager(username, password);

			repository.setAuthenticationManager(authManager);
			return repository;
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

}
