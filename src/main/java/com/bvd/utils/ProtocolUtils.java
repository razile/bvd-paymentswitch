package com.bvd.utils;

import java.nio.charset.StandardCharsets;
import java.util.AbstractMap;

import com.bvd.protocol.STX_ETX_Decoder;
import com.bvd.protocol.STX_ETX_Encoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.ssl.SslContext;

public class ProtocolUtils {
	
	private static final ByteBuf delimiter = Unpooled.copiedBuffer(ASCIIChars.ETXBytes);
	
	
	public static AbstractMap.SimpleEntry<String, String> parseXmlField(String field) {
		
		if (field == null || field.length() < 2) return null;
		
		String key =  field.substring(0,2);
		String value = field.substring(2);
		
		return new AbstractMap.SimpleEntry<String, String>(key, value);	
	}
	
	
	public static void establishPipeline(SslContext sslCtx, ChannelPipeline p) {
		
		if (sslCtx != null) {
             p.addLast(sslCtx.newHandler(ch.alloc()));
        }

        // Add protocol decoders / encoders
         
        // the first decoder will look for the <ETX> end of text ASCII char.  This will produce a substring starting with <STX>
        p.addLast(new DelimiterBasedFrameDecoder(32768, delimiter));
        
        // The STX/ETX codecs will ensure the message is wrapped/stripped of <STX> start and <ETX> end chars
        p.addLast(new STX_ETX_Decoder(StandardCharsets.UTF_8));
        p.addLast(new STX_ETX_Encoder(StandardCharsets.UTF_8));
	}

}
