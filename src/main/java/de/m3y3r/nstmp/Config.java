package de.m3y3r.nstmp;

public enum Config {

	INSTANCE;

	public int getDefaultDataLen() {
		return 1001; // 1000 or 1001 for extra .. -> . handling?
	}

	public int getDefaultCommandLen() {
		return 512;
	}

	public String getDomain() {
		return "localhost";
	}
}
