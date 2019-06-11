package de.m3y3r.nsmtp.command;

import java.util.List;

import de.m3y3r.nsmtp.model.SessionContext;
import de.m3y3r.nsmtp.model.SmtpCommandReply;
import io.netty.channel.ChannelHandlerContext;

public interface SmtpCommand {

	CharSequence getCommandVerb();
	CharSequence getHelloKeyword(SessionContext ctx);
	List<CharSequence> getHelloParams(SessionContext ctx);
	List<CharSequence> getMailParams(SessionContext ctx);
	List<CharSequence> getRecipentParams(SessionContext ctx);
	int getAdditionalDataLen();
	int getAdditionalCommandLen();
	SmtpCommandReply processCommand(SessionContext ctxMailSession, ChannelHandlerContext ctxChannel, CharSequence argument);
}
