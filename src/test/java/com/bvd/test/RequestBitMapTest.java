package com.bvd.test;

import java.nio.charset.Charset;

import com.bvd.paymentswitch.models.PosAuthorization;
import com.bvd.paymentswitch.utils.ProtocolUtils;

public class RequestBitMapTest {

	public static void main(String[] args) {
		
		ProtocolUtils.APP_CHARSET = Charset.forName("windows-1252");
		String req = "KARDALLC1.0�þT2222A      20160920111925103KLJ      31132QWEOAGSUEOKC<P1/><P2/><DI/><LV500.000/>˜";
		
		PosAuthorization auth = new PosAuthorization(req);
		
		System.out.println(auth.getReAuthorizationFlag());

	}

}
