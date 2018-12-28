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
public class Quit implements SmtpCommand {

	@Override
	public CharSequence getCommandWord() {
		return "QUIT";
	}

	@Override
	public SmtpCommandReply processCommand(SessionContext ctxMailSession, ChannelHandlerContext ctxChannel,
			CharSequence argument) {
		SmtpCommandReply reply = new SmtpCommandReply(SmtpReplyStatus.R221, null);
		ctxChannel.writeAndFlush(reply);
		ctxChannel.close();

		return null;
	}

}
