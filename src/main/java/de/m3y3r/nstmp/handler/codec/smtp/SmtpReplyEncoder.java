package de.m3y3r.nstmp.handler.codec.smtp;

import java.nio.charset.StandardCharsets;

import de.m3y3r.nstmp.handler.codec.smtp.model.SmtpCommandReply;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class SmtpReplyEncoder extends MessageToByteEncoder<SmtpCommandReply> {

	@Override
	protected void encode(ChannelHandlerContext ctx, SmtpCommandReply msg, ByteBuf out) throws Exception {
		//TODO: split text after max length
		String rt = String.format("%03d %s\r\n", msg.getReplyCode().getStatus(), msg.getText());
		byte[] bytes = rt.getBytes(StandardCharsets.US_ASCII);
		out.writeBytes(bytes);
	}

}
