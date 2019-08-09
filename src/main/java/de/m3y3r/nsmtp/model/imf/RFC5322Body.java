package de.m3y3r.nsmtp.model.imf;

public class RFC5322Body extends AbstractBody {

	/*
	 * CR and LF MUST only occur together as CRLF; they MUST NOT appear independently in the body
	 */
	@Override
	protected void validateLine(CharSequence line) {
		for(int i = 0, n = line.length(); i < n; i++) {
			char c = line.charAt(i);
			if(c == 13 || c == 10) {
				throw new SoleCRLFException(c);
			}

			// 3.5.  Overall Message Syntax
			if(c < 1 || c > 127)
				throw new InvalidCharaterException(c); // only us-ascii is allowed!
		}
	}

}
