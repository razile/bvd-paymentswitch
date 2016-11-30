package com.bvd.paymentswitch.protocol;

import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.bvd.paymentswitch.utils.ASCIIChars;
import com.bvd.paymentswitch.utils.ProtocolUtils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.string.StringDecoder;

@Component
@Qualifier("comdata-decoder")
public class ComdataDecoder extends StringDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
    	String s = msg.toString(ProtocolUtils.APP_CHARSET);
    	
    	// check for STX 
    	if (s.startsWith(String.valueOf(ASCIIChars.ASC123))) {
    		s = s.substring(1);
    	}
    	
        out.add(s);
    }
}
