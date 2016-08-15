package com.bvd.protocol;

import java.nio.charset.Charset;
import java.util.List;

import com.bvd.utils.ASCIIChars;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.string.StringDecoder;

public class STX_ETX_Decoder extends StringDecoder {
	
    private Charset charset;

	public STX_ETX_Decoder(Charset charset) {
        if (charset == null) {
            throw new NullPointerException("charset");
        }
        this.charset = charset;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
    	String s = msg.toString(charset);
    	
    	// check for STX 
    	if (s.startsWith(String.valueOf(ASCIIChars.STX))) {
    		s = s.substring(1);
    	}
    	
        out.add(s);
    }
}
