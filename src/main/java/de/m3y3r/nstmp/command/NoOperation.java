package de.m3y3r.nstmp.command;

import de.m3y3r.nstmp.model.SessionContext;
import de.m3y3r.nstmp.model.SmtpCommandReply;
import de.m3y3r.nstmp.model.SmtpReplyStatus;
import io.netty.channel.ChannelHandlerContext;

/**
 * some kind of PING
 * @author thomas
 *
 */
public class NoOperation implements SmtpCommand {

	@Override
	public CharSequence getCommandWord() {
		return "NOOP";
	}

	@Override
	public SmtpCommandReply processCommand(SessionContext ctxMailSession, ChannelHandlerContext ctxChannel,
			CharSequence argument) {
		SmtpCommandReply reply = new SmtpCommandReply(SmtpReplyStatus.R250, "OK");
		return reply;
	}

}
