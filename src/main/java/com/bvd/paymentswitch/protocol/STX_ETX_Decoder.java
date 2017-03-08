package com.bvd.paymentswitch.protocol;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.bvd.paymentswitch.utils.ASCIIChars;
import com.bvd.paymentswitch.utils.ProtocolUtils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.string.StringDecoder;

@Component
@Qualifier("decoder")
public class STX_ETX_Decoder extends StringDecoder {
	static final Logger logger = LoggerFactory.getLogger(STX_ETX_Decoder.class);
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
    	
    	if (msg == null || !msg.isReadable()) {
    		logger.error("Invalid data received: " + msg);
    		ctx.close();
    	} else {
    		String s = msg.toString(ProtocolUtils.APP_CHARSET);
	    	
	    	// check for STX 
	    	if (s.startsWith(String.valueOf(ASCIIChars.STX))) {
	    		s = s.substring(1);
	    	}
    	
	    	out.add(s);	
    	}
    }
}
