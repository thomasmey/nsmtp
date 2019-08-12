package de.m3y3r.nsmtp.command;

import de.m3y3r.nsmtp.model.SessionContext;
import de.m3y3r.nsmtp.model.SmtpCommandReply;
import io.netty.channel.ChannelHandlerContext;

public class EightBitMime extends AbstractSmtpCommand {

	
	@Override
	public CharSequence getHelloKeyword(SessionContext ctx) {
		return "8BITMIME";
	}

	@Override
	public CharSequence getCommandVerb() {
		return null;
	}

	@Override
	public SmtpCommandReply processCommand(SessionContext ctxMailSession, ChannelHandlerContext ctxChannel,
			CharSequence argument) {
		throw new IllegalStateException();
	}

}
