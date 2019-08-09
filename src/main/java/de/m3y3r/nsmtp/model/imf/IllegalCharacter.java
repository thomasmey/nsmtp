package de.m3y3r.nsmtp.model.imf;

import de.m3y3r.nsmtp.model.imf.Header.State;

public class IllegalCharacter extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public IllegalCharacter(State state, char c) {
	}

}
