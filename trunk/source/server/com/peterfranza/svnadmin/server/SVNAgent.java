package com.peterfranza.svnadmin.server;

import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

public class SVNAgent {

	static {
		
		String url = ApplicationProperties.getProperty("repository_url");
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
	
	public static SVNRepository getRepository(String reposUrl, String username, String password) {
		SVNRepository repository = null;
		try {
			repository = SVNRepositoryFactory.create(SVNURL
					.parseURIEncoded(reposUrl));

			ISVNAuthenticationManager authManager = SVNWCUtil
					.createDefaultAuthenticationManager(username, password);

			repository.setAuthenticationManager(authManager);
			return repository;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
