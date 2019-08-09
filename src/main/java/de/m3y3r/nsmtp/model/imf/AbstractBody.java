package de.m3y3r.nsmtp.model.imf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractBody {

	private final List<CharSequence> lines;

	public AbstractBody() {
		lines = new ArrayList<CharSequence>();
	}

	public List<CharSequence> getLines() {
		return Collections.unmodifiableList(lines);
	}

	public void addLine(CharSequence line) {
		validateLine(line);
		this.lines.add(line);
	}

	abstract protected void validateLine(CharSequence line);
}
