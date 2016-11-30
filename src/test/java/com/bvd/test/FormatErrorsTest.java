package com.bvd.test;

import java.nio.charset.Charset;

import com.bvd.paymentswitch.utils.ProtocolUtils;

public class FormatErrorsTest {

	public static void main(String[] args) {
		ProtocolUtils.APP_CHARSET = Charset.forName("windows-1252");
		
		String ercd = "CODE:017,TEXT:INVALID UNIT ENTRY/CODE:018,TEXT:INVALID DRID ENTRY";
		
		String e = ProtocolUtils.formatErrors(ercd);
		
		System.out.println(e);

	}

}
