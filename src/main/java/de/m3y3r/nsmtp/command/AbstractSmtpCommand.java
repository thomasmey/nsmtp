package de.m3y3r.nsmtp.command;

import java.util.List;

import de.m3y3r.nsmtp.model.SessionContext;
import de.m3y3r.nsmtp.model.SmtpCommandReply;
import io.netty.channel.ChannelHandlerContext;

public abstract class AbstractSmtpCommand implements SmtpCommand {

	@Override
	public CharSequence getHelloKeyword() {
		return null;
	}

	@Override
	public List<CharSequence> getHelloParams(SessionContext ctx) {
		return null;
	}

	@Override
	public List<CharSequence> getMailParams(SessionContext ctx) {
		return null;
	}

	@Override
	public List<CharSequence> getRecipentParams(SessionContext ctx) {
		return null;
	}

	@Override
	public int getAdditionalDataLen() {
		return 0;
	}

	@Override
	public int getAdditionalCommandLen() {
		return 0;
	}

}
