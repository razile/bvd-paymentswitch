package com.bvd.test;

import java.util.AbstractMap;

//import com.bvd.paymentswitch.utils.ASCIIChars;
import com.bvd.paymentswitch.utils.ProtocolUtils;

public class AsciiCharsTest {

	public static void main(String[] args) {
		
		//char fs = ASCIIChars.ASC47;
		//String msg =  "TSP00007" + fs +  "CARD";

		String e =  "M2" + '\u0000' + "721";
		
		printCodes(e);
		
		
		
		AbstractMap.SimpleEntry<String, String> entry = ProtocolUtils.parseXmlField(e);
		
		String key = entry.getKey();
		String value = entry.getValue().trim();
		
		
		
		System.out.println("Key:" + key + ", Value:" + value);
		
		printCodes(key);
		printCodes(value);
		
		
	}
	
	private static void printCodes(String e) {
		for (char c : e.toCharArray()) {
		    System.out.printf("U+%04x ", (int) c);
		}
		System.out.println();
	}

}
