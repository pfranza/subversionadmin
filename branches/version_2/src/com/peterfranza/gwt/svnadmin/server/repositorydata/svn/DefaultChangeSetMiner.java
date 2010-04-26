package com.peterfranza.gwt.svnadmin.server.repositorydata.svn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;
import org.tmatesoft.svn.core.io.SVNRepository;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.peterfranza.gwt.svnadmin.server.repositorydata.ChangeSet;
import com.peterfranza.gwt.svnadmin.server.repositorydata.ChangeSetMiner;

public class DefaultChangeSetMiner implements ChangeSetMiner {

	Provider<SVNRepository> reposProvider;

	@Inject
	public DefaultChangeSetMiner(Provider<SVNRepository> reposProvider) {
		this.reposProvider = reposProvider;
	}
	
	public ChangeSet getSummary(long revisionNumber) {

		long startRevision = revisionNumber;
		long endRevision = revisionNumber;

		SVNRepository repository = null;
		try {
			repository = reposProvider.get();
			Collection<?> logEntries = repository.log(new String[] { "" },
					null, startRevision, endRevision, true, true);

			for (Iterator<?> entries = logEntries.iterator(); entries.hasNext();) {
				SVNLogEntry logEntry = (SVNLogEntry) entries.next();
				return new ChangeSummaryImpl(logEntry);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(repository != null) { repository.closeSession(); }
		}
		return null;
	}
	
	public static class ChangeSummaryImpl implements ChangeSet {

		private long revision;
		private String author;
		private String message;
		private String changes;
		private Date date;
		private List<String> changesList = new ArrayList<String>();

		private ChangeSummaryImpl(SVNLogEntry logEntry) {
			this.revision = logEntry.getRevision();
			this.author = logEntry.getAuthor();
			this.message = logEntry.getMessage();
			this.date = logEntry.getDate();

			Set<?> changedPathsSet = logEntry.getChangedPaths().keySet();
			StringBuffer buf = new StringBuffer();
			for (Iterator<?> changedPaths = changedPathsSet.iterator(); changedPaths
					.hasNext();) {
				SVNLogEntryPath entryPath = (SVNLogEntryPath) logEntry
						.getChangedPaths().get(changedPaths.next());
				buf
						.append(
								" "
										+ entryPath.getType()
										+ " "
										+ entryPath.getPath()
										+ ((entryPath.getCopyPath() != null) ? " (from "
												+ entryPath.getCopyPath()
												+ " revision "
												+ entryPath.getCopyRevision()
												+ ")"
												: "")).append(
								System.getProperty("line.separator"));
				changesList.add(entryPath.getPath());
				if (entryPath.getCopyPath() != null) {
					changesList.add(entryPath.getCopyPath());
				}
			}

			this.changes = buf.toString();
		}

		public final long getRevision() {
			return revision;
		}

		public final String getAuthor() {
			return author;
		}

		public final String getMessage() {
			return message;
		}

		public final String getChanges() {
			return changes;
		}

		public final Date getDate() {
			return date;
		}

		@Override
		public String toString() {
			StringBuffer buf = new StringBuffer();
			
			buf.append(getMessage())
				.append(System.getProperty("line.separator"))
				.append(System.getProperty("line.separator"))
				.append(System.getProperty("line.separator"));
			
			buf.append("revision: ").append(getRevision()).append(
					System.getProperty("line.separator"));
			buf.append("author: ").append(getAuthor()).append(
					System.getProperty("line.separator"));
			buf.append("date: ").append(getDate()).append(
					System.getProperty("line.separator"));

			buf.append("changes: ")
					.append(System.getProperty("line.separator"));
			buf.append(getChanges()).append(
					System.getProperty("line.separator"));
			return buf.toString().trim();
		}

		public List<String> getChangeSet() {
			return changesList;
		}

	}
	
}
