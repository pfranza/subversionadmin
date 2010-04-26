package com.peterfranza.gwt.svnadmin.server;

import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.peterfranza.gwt.svnadmin.server.entitydata.User;
import com.peterfranza.gwt.svnadmin.server.entitydata.UserManager;
import com.peterfranza.gwt.svnadmin.server.mailer.MailSender;
import com.peterfranza.gwt.svnadmin.server.repositorydata.ChangeSet;
import com.peterfranza.gwt.svnadmin.server.repositorydata.ChangeSetMiner;
import com.peterfranza.gwt.svnadmin.server.repositorydata.Project;
import com.peterfranza.gwt.svnadmin.server.repositorydata.RepositoryManager;

@Singleton
public class MailTriggerServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3948053918529107394L;
	
	private ChangeSetMiner changeMiner;
	private MailSender mailer;
	private RepositoryManager reposManager;
	private UserManager userManager;

	@Inject
	public MailTriggerServlet(ChangeSetMiner changeMiner,
			MailSender mailer,
			RepositoryManager reposManager,
			UserManager userManager) {
		this.changeMiner = changeMiner;
		this.mailer = mailer;
		this.reposManager = reposManager;
		this.userManager = userManager;
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Long transaction = Long.valueOf(req.getParameter("transaction"));
		ChangeSet set = changeMiner.getSummary(transaction);
		String subject = "SVN Commit: (" + set.getRevision() + ")" + set.getMessage();
		subject = subject.length() < 100 ? subject : (subject.substring(0, 100) + "...");		
		mailer.sendMail(
				getUsers(getProjects(set.getChangeSet())),
				userManager.getUserForName(set.getAuthor()),
				subject, 
				set.toString());
	}

	private Set<User> getUsers(Collection<Project> projects) {
		TreeSet<User> users = new TreeSet<User>(new Comparator<User>() {
			@Override
			public int compare(User o1, User o2) {
				return o1.getEmailAddress().trim().compareToIgnoreCase(o2.getEmailAddress().trim());
			}
		});
		
		for(User u: userManager.getUsers()) {
			for(Project p: projects) {
				if(reposManager.isSubscribed(p.getPath(), u)) {
					users.add(u);
				}
			}
		}
		
		return users;
	}
	
	private Set<Project> getProjects(List<String> changeSet) {
		TreeSet<Project> projs = new TreeSet<Project>(new Comparator<Project>() {
			@Override
			public int compare(Project o1, Project o2) {
				return o1.getPath().compareToIgnoreCase(o2.getPath());
			}
		});
		for(Project p: reposManager.getProjects()) {
			for(String c: changeSet) {
				if(c.startsWith(p.getPath())) {
					projs.add(p);
				}
			}
		}
		return projs;
	}
	
}
