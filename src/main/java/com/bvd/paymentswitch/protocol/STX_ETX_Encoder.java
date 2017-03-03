package com.bvd.paymentswitch.protocol;

import java.nio.CharBuffer;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.bvd.paymentswitch.utils.ASCIIChars;
import com.bvd.paymentswitch.utils.ProtocolUtils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.string.StringEncoder;

@Component
@Qualifier("encoder")
public class STX_ETX_Encoder extends StringEncoder {
	static final Logger logger = LoggerFactory.getLogger(STX_ETX_Encoder.class);
	
    @Override
    protected void encode(ChannelHandlerContext ctx, CharSequence msg, List<Object> out) throws Exception {
       // if (msg.length() == 0) {
       //     return;
       // }
        
        String message = msg.toString();
        message = ASCIIChars.STX + message + ASCIIChars.ETX;
        CharBuffer cb = CharBuffer.wrap(message.toCharArray());
        ByteBuf bytes = ByteBufUtil.encodeString(ctx.alloc(), cb, ProtocolUtils.APP_CHARSET);
        out.add(bytes);
        logger.debug("SEND: " + message);
    }
}
