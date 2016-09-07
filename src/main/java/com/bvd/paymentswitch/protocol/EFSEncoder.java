package com.bvd.paymentswitch.protocol;

import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.bvd.paymentswitch.utils.ASCIIChars;
import com.bvd.paymentswitch.utils.ProtocolUtils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.string.StringEncoder;

@Component
@Qualifier("efs-encoder")
public class EFSEncoder extends StringEncoder {

    @Override
    protected void encode(ChannelHandlerContext ctx, CharSequence msg, List<Object> out) throws Exception {
       // if (msg.length() == 0) {
       //     return;
       // }
        
        String message = msg.toString();
        message = ASCIIChars.STX + "PV|" +  message + ASCIIChars.ETX + "\n\r";
        CharBuffer cb = CharBuffer.wrap(message.toCharArray());
        ByteBuf bytes = ByteBufUtil.encodeString(ctx.alloc(), cb, ProtocolUtils.APP_CHARSET);
        out.add(bytes);
    }
}
