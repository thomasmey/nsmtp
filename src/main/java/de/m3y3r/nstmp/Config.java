package de.m3y3r.nstmp;

public enum Config {

	INSTANCE;

	public int getMaxDataLen() {
		return 1001; // 1000 or 1001 for extra .. -> . handling?
	}

	public int getMaxCommandLen() {
		return 512;
	}

	public String getDomain() {
		return "localhost";
	}
}
