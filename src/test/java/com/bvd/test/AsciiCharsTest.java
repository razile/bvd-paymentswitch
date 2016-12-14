package com.bvd.test;

import com.bvd.paymentswitch.utils.ASCIIChars;

public class AsciiCharsTest {

	public static void main(String[] args) {
		
		char fs = ASCIIChars.ASC47;
		String msg =  "TSP00007" + fs +  "CARD";

		System.out.println(msg);
				
		
	}

}
