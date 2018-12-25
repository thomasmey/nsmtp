package de.m3y3r.nstmp.handler.codec.smtp;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import de.m3y3r.nstmp.handler.codec.smtp.model.MailTransaction;
import de.m3y3r.nstmp.handler.codec.smtp.model.SmtpCommandReply;
import de.m3y3r.nstmp.handler.codec.smtp.model.SmtpReplyStatus;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.smtp.SmtpCommand;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

/**
 * RFC2821 SMTP server
 *
 * https://tools.ietf.org/html/rfc2821
 * @author thomas
 *
 */
public class SmtpCommandHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf frame = (ByteBuf) msg;

		if(!isSessionStarted(ctx)) {
			/* we did receive an command before we send an initial greetings reply */
			/* what to do here? is it possible to send a reply here?
			 */
			return;
		}

		String line = frame.toString(StandardCharsets.US_ASCII);
		System.out.println(line);

		if(line.length() < 4) {
			throw new UnknownCommandException(line);
		}

		String cmd = line.substring(0, 4);
		String argument = line.length() > 4 ? line.substring(4) : null;

		validateCommand(cmd);
		validateCommandOrder(ctx, cmd);

		/* process command */

		/* 4.5.1 Minimum Implementation */
		switch (cmd) {
		case "EHLO":
		{
			resetState(ctx);
			String domainOrAddressLiteral = argument;
			String greeting = "Greetings from Netty SMTP server";
			Object reply = new SmtpCommandReply(SmtpReplyStatus.R250, domainOrAddressLiteral + " " + greeting);
			ctx.writeAndFlush(reply);
			break;
		}

		case "HELO":
		{
			Object reply = new SmtpCommandReply(SmtpReplyStatus.R502, "TODO");
			ctx.writeAndFlush(reply);
		}

		case "MAIL":
		{
			Attribute<MailTransaction> mailTx = ctx.channel().attr(AttributeKey.valueOf("mailTransaction"));
			MailTransaction mailTxR = new MailTransaction();
			mailTx.set(mailTxR);
			mailTxR.setFrom(argument);

			Object reply = new SmtpCommandReply(SmtpReplyStatus.R250, "OK");
			ctx.writeAndFlush(reply);
			break;
		}

		case "RCPT":
		{
			Attribute<MailTransaction> mailTx = ctx.channel().attr(AttributeKey.valueOf("mailTransaction"));
			MailTransaction mailTxR = mailTx.get();
			mailTxR.addTo(argument);

			Object reply = new SmtpCommandReply(SmtpReplyStatus.R250, "OK");
			ctx.writeAndFlush(reply);
			break;
		}

		case "DATA":
		{
			Object reply = new SmtpCommandReply(SmtpReplyStatus.R354, "Start mail input; end with <CRLF>.<CRLF>");
			ctx.writeAndFlush(reply);
			ctx.pipeline().replace(this, "smptInData", new SmtpDataHandler());
			break;
		}

		case "QUIT":
		{
			Object reply = new SmtpCommandReply(SmtpReplyStatus.R221, null);
			ctx.writeAndFlush(reply);
			ctx.close();
			break;
		}

		/* order-independent commands:
		 * "The NOOP, HELP, EXPN, VRFY, and RSET commands can be used at any time during a session"
		 */
		case "RSET":
		{
			resetState(ctx); // abort any ongoing mail transaction
			Object reply = new SmtpCommandReply(SmtpReplyStatus.R250, "OK");
			ctx.writeAndFlush(reply);
			break;
		}
		case "VRFY":
		{
			boolean isValid = verifyUserOrMailbox(argument);
			Object reply;
			if(isValid)
				reply = new SmtpCommandReply(SmtpReplyStatus.R250, "OK");
			else
				reply = new SmtpCommandReply(SmtpReplyStatus.R250, "OK");

			ctx.writeAndFlush(reply);
			break;
		}
		case "EXPN":
		{
			Object reply = new SmtpCommandReply(SmtpReplyStatus.R502, "TODO");
			ctx.writeAndFlush(reply);
			break;
		}
		case "HELP":
		{
			Object reply = new SmtpCommandReply(SmtpReplyStatus.R250, "OK");
			ctx.writeAndFlush(reply);
			break;
		}
		case "NOOP":
		{
			Object reply = new SmtpCommandReply(SmtpReplyStatus.R250, "OK");
			ctx.writeAndFlush(reply);
			break;
		}

		default:
		{
			Object reply = new SmtpCommandReply(SmtpReplyStatus.R500, "WAT?");
			ctx.writeAndFlush(reply);
			break;
		}
		}
	}

	private void resetState(ChannelHandlerContext ctx) {
		Attribute<MailTransaction> mailTx = ctx.channel().attr(AttributeKey.valueOf("mailTransaction"));
		mailTx.set(null);
	}

	/**
	 * was this session started at all, by sending an greetings reply?
	 * @param ctx
	 * @return 
	 */
	private boolean isSessionStarted(ChannelHandlerContext ctx) {
		Attribute<Boolean> sessionStarted = ctx.channel().attr(AttributeKey.valueOf("sessionStarted"));
		return sessionStarted.get() != null ? sessionStarted.get() : false;
	}

	private boolean verifyUserOrMailbox(String argument) {
		return false;
	}

	/**
	 * is the command allowed in the current state
	 * @param ctx
	 * @param currentCmd
	 */
	private void validateCommandOrder(ChannelHandlerContext ctx, String currentCmd) {
		Attribute<String> lastCmd = ctx.channel().attr(AttributeKey.valueOf("lastCmd"));
		if(currentCmd.equals("HELO") || currentCmd.equals("EHLO") || currentCmd.equals("RSET"))
			return;

		if(lastCmd.get() == null) {
			return;
		}
	
		if(
			currentCmd.equals("RCPT") && lastCmd.get().equals("MAIL") ||
			currentCmd.equals("RCPT") && lastCmd.get().equals("RCPT") ||
			currentCmd.equals("DATA") && lastCmd.get().equals("RCPT")
		)
			return;

		throw new IllegalStateException();
	}

	private static final String[] VALID_COMMANDS = new String[] {"HELO", "EHLO", "MAIL", "RSET", "VRFY", "EXPN", "NOOP", "QUIT"};

	/**
	 * is this an valid SMTP command?
	 * @param cmd
	 */
	private boolean validateCommand(String cmd) {
		return Arrays.stream(VALID_COMMANDS).anyMatch(cmd::equals);
	}
}
