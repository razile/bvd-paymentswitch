package com.bvd.client;

import com.bvd.server.PaymentSwitchInitializer;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

public class BVDClient {

	public static void main(String[] args) throws Exception {
		
		String HOST; 
		if (args.length > 0) {
			HOST = args[0];
		} else {
			HOST = "127.0.0.1";
		}
		
		int PORT;
		if (args.length > 1) {
			PORT = Integer.parseInt(args[1]);
		} else {
			PORT = 10085;
		}
		
		boolean SSL = false;
		if (args.length > 2) {
			if (args[2].equalsIgnoreCase("ssl")) {
				SSL = true;
			}
		}
		
		final SslContext sslCtx;
	    if (SSL) {
	    	sslCtx = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
	    } else {
	    	sslCtx = null;
	    }
		
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
             .channel(NioSocketChannel.class)
             .handler(new BVDClientInitializer(sslCtx));

            // Make the connection attempt.
            Channel ch = b.connect(HOST, PORT).sync().channel();
            // Create the request
       
            //String request = "KARDALLC1.0ˆþELMVALE     2016072811441200000000000057768J4371LW3M875<C1606017000000027365/><N11017/><F1300/>y";
            String request = "KARDALLC1.0^þELMVALE     2016072811441200000000000057768J4371LW3M875<C1606017000000027365/><N11017/><F1300/>y";
            
            // Send the Kardall request
            ch.writeAndFlush(request);

            // Wait for the server to close the connection.
            ch.closeFuture().sync();

        } finally {
            // The connection is closed automatically on shutdown.
            group.shutdownGracefully();
        }
    }
}