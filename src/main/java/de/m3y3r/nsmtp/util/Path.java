package de.m3y3r.nsmtp.util;

import java.util.ArrayList;
import java.util.List;

public class Path {

	private CharSequence mailbox;
	private List<Domain> routes;

	public Path() {
		routes = new ArrayList<Domain>();
	}

	public static Path parse(CharSequence argument) {

		//FIXME: not sure about 256 for path, see rfc5321 - "4.5.3.1.3"
		if(argument == null || argument.length() == 0 || argument.length() > 256) {
			return null;
		}

		// check bounds
		if(argument.charAt(0) != '<' && argument.charAt(argument.length() - 1) != '>') {
			return null;
		}

		int mode = 0;
		if(argument.length() > 1) {
			if(argument.charAt(1) == '@')
				mode = 2;
			else
				mode = 1;
		}
		if(mode == 0) {
			return null;
		}

		Path path = new Path();
		StringBuilder mailbox = new StringBuilder();
		StringBuilder domain = new StringBuilder();

		// mode 2 = in-adl;
		// mode 1 = mailbox
		char lc = 0;
		for(int i = mode, n = argument.length() - 1; i < n; i++) {
			char cc = argument.charAt(i);

			switch(mode) {
			case 2: // in-adl
				if(cc == ':') { // end of a-d-l
					mode = 1;
					Domain d = Domain.parse(domain);
					if(d == null)
						return null;
					path.addRoute(d);
				} else if(cc == ',') {
					Domain d = Domain.parse(domain);
					if(d == null)
						return null;
					path.addRoute(d);
					domain.setLength(0);
				} else if(cc == '@') {
					if(lc != ',')
						return null;
				} else {
					domain.append(cc);
				}
				break;
			case 1: //mailbox
				mailbox.append(cc);
			}

			lc = cc;
		}

		if(!validateMailbox(mailbox)) {
			return null;
		}

		path.setMailbox(mailbox.toString());
		return path;
	}

	private void setMailbox(CharSequence mailbox) {
		this.mailbox = mailbox;
	}

	private void addRoute(Domain domain) {
		routes.add(domain);
	}

	private static boolean validateMailbox(CharSequence mailbox) {
		if(mailbox == null || mailbox.length() < 3)
			return false;

		int mode = 0;
		// 1 == dot-string
		// 2 == quoted-string
		// 3 == domain-part

		if(mailbox.charAt(0) == '"') {
			mode = 2; // quoted-string
		} else {
			mode = 1; // dot-string
		}

		// 1. validate local-part
		char lc = 0;
		out:
		for(int i = 0, n = mailbox.length(); i < n; i++) {
			char cc = mailbox.charAt(i);

			switch(mode) {
			case 1: // dot-string - atext start
				// fun fact: rfc5321 misses the definition of "atext", but rfc5322 has it...
				if(cc == '.' && i == 0) { // no dot on start or end! and not '..' (no multiple dots without atext in between)!
					return false;
				}
				if(i > 0 && cc == '.' && lc == '.')
					return false;
				if(cc == '@') { // @ == end of local-part
					if(lc == '.')
						return false;

					mode = 3;
				} else if(cc == '.') {
				} else {
					if(!isValidAtext(cc))
						return false;
				}
				break;
			case 2:
				return false; // who has a quoted local-part anyway?
			case 3: // domain-part
				Domain d = Domain.parse(mailbox.subSequence(i, mailbox.length()));
				if(d == null) {
					return false;
				}
				break out;
			}
			lc = cc;
		}

		return true;
	}

	/* see RFC2822 for atext definition */
	private static final String ATEXT = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPRSTUVWXYZ!#$%&'*+-/=?^_`{}|~";
	private static boolean isValidAtext(char cc) {
		return ATEXT.contains(Character.toString(cc));
	}
}
