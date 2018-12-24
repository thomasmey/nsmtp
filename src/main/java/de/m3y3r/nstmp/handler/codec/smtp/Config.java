package de.m3y3r.nstmp.handler.codec.smtp;

public enum Config {

	INSTANCE;

	public int getMaxCommandLen() {
		return 512;
	}

	public String getDomain() {
		return "localhost";
	}
}
