package com.bvd.protocol;

import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.bvd.utils.ASCIIChars;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.string.StringEncoder;

public class STX_ETX_Encoder extends StringEncoder {
	
	private Charset charset;

	public STX_ETX_Encoder(Charset charset) {
        if (charset == null) {
            this.charset = StandardCharsets.UTF_8;
        }
        this.charset = charset;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, CharSequence msg, List<Object> out) throws Exception {
        if (msg.length() == 0) {
            return;
        }
        
        String message = msg.toString();
        message = ASCIIChars.STX + message + ASCIIChars.ETX;
        CharBuffer cb = CharBuffer.wrap(message.toCharArray());
  
        ByteBuf bytes = ByteBufUtil.encodeString(ctx.alloc(), cb, charset);

      //  String message = bytes.toString(charset);
        out.add(bytes);
    }
}
