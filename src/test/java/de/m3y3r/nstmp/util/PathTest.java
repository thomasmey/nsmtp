package de.m3y3r.nstmp.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class PathTest {

	@Test
	public void test() {
		Path p1 = Path.parse("<@hosta.int,@jkl.org:userc@d.bar.org>");
		Path p2 = Path.parse("<userx@y.foo.org>");
	}

}
