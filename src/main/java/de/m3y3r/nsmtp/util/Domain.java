package de.m3y3r.nsmtp.util;

public class Domain {

	private final CharSequence domain;

	public Domain(CharSequence domain) {
		this.domain = domain;
	}

	public static Domain parse(CharSequence domain) {
		if(domain == null || domain.length() == 0 || domain.length() > 255)
			return null;

		if(domain.charAt(domain.length() - 1) == '-') { // must not be the last char in the ldh-str
			return null;
		}

		// sub-domain = let-dig [*( ALPHA / DIGIT / "-" ) Let-dig]

		int mode = 1;
		// 1 == let-dig
		// 2 == ldh-str

		char lc = 0;
		out:
			for(int i = 0, n = domain.length(); i < n; i++) {
				char cc = domain.charAt(i);

				switch(mode) {
				case 1: // let-dig
					if(!isLetDig(cc)) {
						return null;
					}
					mode = 2;
					break;
				case 2: // ldh-str
					if(cc == '.') { // end of sub-domain
						if(lc == '-') { // must not be the last char in the ldh-str
							return null;
						}
						mode = 1;
						break;
					}
					if(!(isLetDig(cc) || cc == '-')) {
						return null;
					}
				}
				lc = cc;
			}

		return new Domain(domain.toString());
	}

	private static boolean isLetDig(char cc) {
		if (
			cc >= '0' && cc <= '9' ||
			cc >= 'a' && cc <= 'z' ||
			cc >= 'A' && cc <= 'Z'
			)
			return true;
		return false;
	}

	public CharSequence getDomain() {
		return domain;
	}
}
