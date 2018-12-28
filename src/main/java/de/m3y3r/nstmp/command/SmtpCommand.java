package de.m3y3r.nstmp.command;

import de.m3y3r.nstmp.model.SessionContext;
import de.m3y3r.nstmp.model.SmtpCommandReply;
import io.netty.channel.ChannelHandlerContext;

public interface SmtpCommand {

	CharSequence getCommandWord();
	SmtpCommandReply processCommand(SessionContext ctxMailSession, ChannelHandlerContext ctxChannel, CharSequence argument);
}
