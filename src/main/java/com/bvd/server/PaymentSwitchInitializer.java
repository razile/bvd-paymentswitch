package com.bvd.server;

import com.bvd.utils.ProtocolUtils;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.ssl.SslContext;

public class PaymentSwitchInitializer extends ChannelInitializer<SocketChannel> {

	
    private final SslContext sslCtx;
  
    public PaymentSwitchInitializer(SslContext sslCtx) {
        this.sslCtx = sslCtx;
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
    	ChannelPipeline p = ch.pipeline();
        ProtocolUtils.establishPipeline(sslCtx, p);
        // add the handler class containing processing business logic
        p.addLast(new PaymentSwitchHandler());
    }
}
