package com.bvd.client;

import java.nio.charset.StandardCharsets;

import com.bvd.protocol.STX_ETX_Decoder;
import com.bvd.protocol.STX_ETX_Encoder;
import com.bvd.utils.ASCIIChars;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.ssl.SslContext;

public class BVDClientInitializer extends ChannelInitializer<SocketChannel> {

	private static final ByteBuf delimiter = Unpooled.copiedBuffer(ASCIIChars.ETXBytes);
    private final SslContext sslCtx;
  
    public BVDClientInitializer(SslContext sslCtx) {
        this.sslCtx = sslCtx;
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
    	 ChannelPipeline p = ch.pipeline();
         if (sslCtx != null) {
             p.addLast(sslCtx.newHandler(ch.alloc()));
         }

        // Add protocol decoders / encoders
         
        // the first decoder will look for the <ETX> end of text ASCII char.  This will produce a substring starting with <STX>
        p.addLast(new DelimiterBasedFrameDecoder(32768, delimiter));
        
        // The STX/ETX codecs will ensure the message is wrapped/stripped of <STX> start and <ETX> end chars
        p.addLast(new STX_ETX_Decoder(StandardCharsets.UTF_8));
        p.addLast(new STX_ETX_Encoder(StandardCharsets.UTF_8));

        // add the handler class containing processing business logic
        p.addLast(new BVDClientHandler());
    }
}
