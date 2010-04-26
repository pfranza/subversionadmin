package com.peterfranza.gwt.svnadmin.server.mailer;

import java.util.Collection;

import com.google.inject.ImplementedBy;
import com.peterfranza.gwt.svnadmin.server.entitydata.User;

@ImplementedBy(DefaultMailSender.class)
public interface MailSender{
	void sendMail(Collection<User> entities, User from, String subject, String body);
}
