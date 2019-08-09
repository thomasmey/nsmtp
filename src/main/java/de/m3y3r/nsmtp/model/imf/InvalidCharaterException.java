package de.m3y3r.nsmtp.model.imf;

public class InvalidCharaterException extends RuntimeException {

	private char c;

	public InvalidCharaterException(char c) {
		this.c = c;
	}

	@Override
	public String getMessage() {
		return "Body contains invalid character " + c;
	}

}
