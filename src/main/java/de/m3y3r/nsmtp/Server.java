package de.m3y3r.nsmtp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Channel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.m3y3r.nsmtp.handler.codec.smtp.SessionInitiationHandler;
import de.m3y3r.nsmtp.handler.codec.smtp.SmtpCommandHandler;
import de.m3y3r.nsmtp.handler.codec.smtp.SmtpReplyEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;

/**
 * SMTP delivery system
 * https://tools.ietf.org/html/rfc5321
 *
 * @author thomas
 *
 */
public class Server implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(Server.class.getName());

	private int port;

	public Server(int port) {
		this.port = port;
	}

	public static void main(String... args) {
		int smtpPort = 2525;
		Server s = new Server(smtpPort);
		s.run();
	}

	@Override
	public void run() {
		EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();

			// let systemd bind the port and provide it via fd0/1
			java.nio.channels.ServerSocketChannel ssc;
			Channel ic = System.inheritedChannel();
			if(ic instanceof java.nio.channels.ServerSocketChannel) {
				ssc = (java.nio.channels.ServerSocketChannel) ic;
			} else {
				ssc = java.nio.channels.ServerSocketChannel.open();
				ssc.bind(new InetSocketAddress(port));
			}

			logger.info("Starting nstmp with socket {}", ssc);
			b.group(eventLoopGroup)
				.channelFactory(() -> new NioServerSocketChannel(ssc))
//				.channel(NioServerSocketChannel.class)
//				.localAddress(new InetSocketAddress(port))
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast("smtpOutReply", new SmtpReplyEncoder());
						ch.pipeline().addLast("smptInSession", new SessionInitiationHandler());
						ch.pipeline().addLast("smtpInLine", new DelimiterBasedFrameDecoder(Config.INSTANCE.getDefaultCommandLen(), true, Delimiters.lineDelimiter()[0]));
						ch.pipeline().addLast("smptInCommand", new SmtpCommandHandler());
						ch.pipeline().addLast("exceptionLogger", new ExceptionLogger());
					}
				});
			ChannelFuture f = b.register().sync();
			f.channel().closeFuture().sync();
		} catch (InterruptedException | IOException e) {
			logger.error("Int1", e);
		} finally {
			try {
				eventLoopGroup.shutdownGracefully().sync();
			} catch (InterruptedException e) {
				logger.error("Int2", e);
			}
		}
	}
}
