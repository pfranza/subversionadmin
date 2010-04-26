package com.peterfranza.gwt.svnadmin.server.util;

import com.google.inject.ImplementedBy;

@ImplementedBy(DefaultCrypt.class)
public interface Crypt {

	/**
	 * <P>
	 * Encrypt a password given the cleartext password. This method generates a
	 * random salt using the 'java.util.Random' class.
	 * </P>
	 * 
	 * @param original
	 *            The password to be encrypted.
	 * @return A string consisting of the 2-character salt followed by the
	 *         encrypted password.
	 */
	public abstract String crypt(String original);

	/**
	 * <P>
	 * Check that <I>enteredPassword</I> encrypts to <I>encryptedPassword</I>.
	 * </P>
	 * 
	 * @param encryptedPassword
	 *            The <I>encryptedPassword</I>. The first two characters are
	 *            assumed to be the salt. This string would be the same as one
	 *            found in a Unix <U>/etc/passwd</U> file.
	 * @param enteredPassword
	 *            The password as entered by the user (or otherwise aquired).
	 * @return <B>true</B> if the password should be considered correct.
	 */
	public abstract boolean matches(String encryptedPassword,
			String enteredPassword);

}