package de.m3y3r.nstmp.handler.codec.smtp;

import java.nio.charset.StandardCharsets;

import de.m3y3r.nstmp.handler.codec.smtp.model.SmtpCommandReply;
import de.m3y3r.nstmp.handler.codec.smtp.model.SmtpReplyStatus;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.smtp.SmtpCommand;

/**
 * RFC2821 SMTP server
 *
 * https://tools.ietf.org/html/rfc2821
 * @author thomas
 *
 */
public class SmtpCommandHandler extends LineBasedFrameDecoder {

	public SmtpCommandHandler() {
		super(Config.INSTANCE.getMaxCommandLen());
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf buffer) throws Exception {
		String line = buffer.toString(StandardCharsets.US_ASCII);

		SmtpCommand cmd = SmtpCommand.valueOf(line.subSequence(0, 4));

		/* 4.5.1 Minimum Implementation */
		switch (cmd.name().toString()) {
		case "EHLO":
		{
			String domain = line.substring(4);
			String greeting = "Greeetings-from Netty SMTP server";
			Object reply = new SmtpCommandReply(SmtpReplyStatus.R250, domain + " " + greeting);
			ctx.writeAndFlush(reply);
			break;
		}

		case "HELO":
		{
			Object reply = new SmtpCommandReply(SmtpReplyStatus.R250, null);
			ctx.writeAndFlush(reply);
			break;
		}

		case "MAIL":
			break;
		case "RCPT":
			break;
		case "RSET":
			break;
		case "VRFY":
			break;
		case "EXPN":
			break;
		case "HELP":
			break;
		case "NOOP":
			break;
		case "QUIT":
		{
			Object reply = new SmtpCommandReply(SmtpReplyStatus.R221, null);
			ctx.writeAndFlush(reply);
			break;
		}

		default:
			// unknown command 5 something
//			Object reply = new SmtpCommandReply(SmtpReplyStatus.Rxxx, null);
//			ctx.writeAndFlush(reply);
			break;
		}
		System.out.println("called");
		return super.decode(ctx, buffer);
	}
}
