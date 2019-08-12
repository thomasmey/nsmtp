package de.m3y3r.nsmtp.model.imf;

public class InvalidCharaterException extends RuntimeException {

	private byte c;

	public InvalidCharaterException(byte c) {
		this.c = c;
	}

	@Override
	public String getMessage() {
		return "Body contains invalid character " + c;
	}

}
