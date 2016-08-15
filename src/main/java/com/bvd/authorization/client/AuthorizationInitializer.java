package com.bvd.authorization.client;

import java.nio.charset.StandardCharsets;

import com.bvd.protocol.STX_ETX_Decoder;
import com.bvd.protocol.STX_ETX_Encoder;
import com.bvd.utils.ASCIIChars;
import com.bvd.utils.ProtocolUtils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.ssl.SslContext;

public class AuthorizationInitializer extends ChannelInitializer<SocketChannel> {

    private final SslContext sslCtx;
    private Channel inboundChannel;
  
    public AuthorizationInitializer(SslContext sslCtx, Channel inboundChannel) {
        this.sslCtx = sslCtx;
        this.inboundChannel = inboundChannel;
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
    	ChannelPipeline p = ch.pipeline();
        if (sslCtx != null) {
        	 p.addLast(sslCtx.newHandler(ch.alloc()));
        }

        ProtocolUtils.establishPipeline(sslCtx, p);
      
        // add the handler class containing processing business logic
        p.addLast(new AuthorizationHandler(inboundChannel));
    }
}
