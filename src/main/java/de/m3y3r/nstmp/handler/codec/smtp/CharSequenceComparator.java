package de.m3y3r.nstmp.handler.codec.smtp;

public class CharSequenceComparator {

	public static boolean equals(CharSequence c1, CharSequence c2) {
		if(c1 == null && c2 == null)
			return true;

		if(
			c1 != null && c2 == null ||
			c1 == null && c2 != null
		)
			return false;

		if(c1.length() != c2.length())
			return false;

		for(int i = 0, n = c1.length(); i < n; i++) {
			if(c1.charAt(i) != c2.charAt(i))
				return false;
		}

		return true;
	}
}
