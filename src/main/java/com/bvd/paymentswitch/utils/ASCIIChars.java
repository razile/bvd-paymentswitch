package com.bvd.paymentswitch.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class ASCIIChars {
	
	
	public final static byte[] ETX_Bytes = {0x03};
	public final static byte[] STX_Bytes = {0x02};
	public final static byte[] ASC125_Bytes = {0x7D};

	public final static char ETX = '\u0003';
	public final static char STX = '\u0002';
	public static  char ASC123 = '\u0173';
	public static  char ASC125 = '\u0175';
	public static char ASC28= '\u0034';
	public static char ASC47 = '\u0057';
	
	

	public final static char LF = 0x0A;
	public final static char CR = 0x0D;
	
	

	public final static ByteBuf ETX_DELIMITER = Unpooled.copiedBuffer(ETX_Bytes);
	public final static ByteBuf ASC125_DELIMITER = Unpooled.copiedBuffer(ASC125_Bytes);
	
	
	
	public final static byte[] NAK_Bytes = {0x15};
	public final static char NAK = '\u0015';
	

}
