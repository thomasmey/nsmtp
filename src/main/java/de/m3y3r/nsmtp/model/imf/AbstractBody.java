package de.m3y3r.nsmtp.model.imf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.netty.buffer.ByteBuf;

public abstract class AbstractBody {

	private final List<ByteBuf> lines;

	public AbstractBody() {
		lines = new ArrayList<>();
	}

	public List<ByteBuf> getLines() {
		return Collections.unmodifiableList(lines);
	}

	public void addLine(ByteBuf line) {
		validateLine(line);
		this.lines.add(line);
	}

	protected abstract void validateLine(ByteBuf line);

	protected void validate() {};
}
