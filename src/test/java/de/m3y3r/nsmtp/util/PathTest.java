package de.m3y3r.nsmtp.util;

import static org.junit.Assert.*;

import org.junit.Test;

import de.m3y3r.nsmtp.util.Path;

public class PathTest {

	@Test
	public void test() {
		Path p1 = Path.parse("<@hosta.int,@jkl.org:userc@d.bar.org>");
		Path p2 = Path.parse("<userx@y.foo.org>");
	}

}
