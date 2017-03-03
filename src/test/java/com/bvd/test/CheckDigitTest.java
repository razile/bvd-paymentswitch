package com.bvd.test;

import java.nio.charset.Charset;

import com.bvd.paymentswitch.utils.ProtocolUtils;

public class CheckDigitTest {

	public static void main(String[] args) {
		
		ProtocolUtils.APP_CHARSET = Charset.forName("windows-1252");
		String ctxt = "KARDALLC1.0­þT2222A      2016090808420900000000000030754UWJRPCXMIZTO<M2/>";
		
		int code = ProtocolUtils.calculateCheckDigit(ctxt);
		String digit = ProtocolUtils.getCharacter(code);
		
		//ctxt = ctxt + c;
		
		System.out.println("Int: " + ProtocolUtils.encodeInteger(code) + ", Digit: " + digit);
		
		for (int i = 1; i<256; i++) {
		    String ch = ProtocolUtils.getCharacter(i);
		    char c = (char)i;
		    String iStr = ProtocolUtils.encodeInteger(i);
		    String bin = ProtocolUtils.getBinary(iStr);
		    int binInt = ProtocolUtils.getInteger(bin);
		    
		    System.out.println("i:" + i + ", ch:'" + ch + "'" + ", c:'" + c + "'" + ", iStr:" + iStr + ", bin:" + bin + ", binInt:" + binInt);
		}
		
		
		
		
	}

}
