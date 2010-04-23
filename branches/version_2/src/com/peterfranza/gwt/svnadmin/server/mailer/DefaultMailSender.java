package com.peterfranza.gwt.svnadmin.server.mailer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.peterfranza.gwt.svnadmin.server.entitydata.User;

public class DefaultMailSender implements MailSender{

	private String fromAddress;
	private String smtpServer;


	@Inject
	public DefaultMailSender(
			@Named("smtpServer") String smtpServer,
			@Named("smtpFromAddress") String fromAddress
			) {
		this.smtpServer = smtpServer;
		this.fromAddress = fromAddress;
	}
	
	@Override
	public void sendMail(Collection<User> entities, String subject,
			String body) {
		sendMail(smtpServer, fromAddress, asArray(entities), subject, body);
	}
	
	
	private String[] asArray(Collection<User> entities) {
		ArrayList<String> l = new ArrayList<String>();
		for(User u: entities) {
			l.add(u.getEmailAddress());
		}
		return l.toArray(new String[0]);
	}

	public static void sendMail(String smtpServer, String fromAddress,
			String[] recipients, String subject, String body) {
		Properties props = new Properties();
		props.put("mail.smtp.host", smtpServer);
		Session session = Session.getInstance(props, null);

		try {
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(fromAddress));
			for (String to : recipients) {
				msg.addRecipient(Message.RecipientType.TO, new InternetAddress(
						to));
			}
			msg.setSubject(subject);
			msg.setSentDate(new Date());
			msg.setText(body);
			Transport.send(msg);
		} catch (MessagingException mex) {
			System.err.println("send failed, exception: " + mex);
		}
	}

}
