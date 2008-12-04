package com.peterfranza.svnadmin.server;

import com.peterfranza.svnadmin.server.ChangeMiner.ChangeSummary;

public class ChangeSetEmailer {

	public static void main(String[] args) {

		ChangeSummary set = ChangeMiner.getSummary(ApplicationProperties
				.getProperty("repository_url"), ApplicationProperties
				.getProperty("repository_username"), ApplicationProperties
				.getProperty("repository_password"), Long.valueOf(args[0]));

		System.out.println(set);
		String[] to = { "pfranza@gmail.com" };
		String subject = "svn commit: " + set.getMessage();
		MailSender.sendMail(ApplicationProperties.getProperty("smtp_server"),
				"pfranza@home.com", to,
				subject.length() < 100 ? subject
						: (subject.substring(0, 100) + "..."), set.toString());
	}

}
