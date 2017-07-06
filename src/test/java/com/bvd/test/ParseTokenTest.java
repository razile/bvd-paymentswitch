package com.bvd.test;

import java.nio.charset.Charset;
import java.util.AbstractMap;

//import com.bvd.paymentswitch.utils.ASCIIChars;
import com.bvd.paymentswitch.utils.ProtocolUtils;

public class ParseTokenTest {

	public static void main(String[] args) {
		
		ProtocolUtils.APP_CHARSET = Charset.forName("windows-1252");
		
		String token = "DRID:V23456";
		
		AbstractMap.SimpleEntry<String, String> entry = ProtocolUtils.parseToken(token);
		
		String key = entry.getKey();
		String value = entry.getValue().trim();
		
		
		
		System.out.println("Key:" + key + ", Value:" + value);
		
		System.out.println("Formatted: " + formatPosPrompt(value));
	}
	
	
	public static String formatPosPrompt(String processorValue) {
		if (processorValue != null) {
			String formattedValue = null;
			
			String minValue = null;
			String exactValue = null;
			String maxValue = null;
			
			boolean isDataVal = false;
			boolean isExact = false;
			boolean hasMin = false;
			boolean hasMax = false;
			
			String[] masks = processorValue.split(";");
			
			for (String mask:masks) {
					
				if (mask.equals("TN")) {
					isDataVal = true;
				}
				
				if (mask.startsWith("X")) {
					hasMax = true;
					maxValue = mask;
				}
				
				if (mask.startsWith("M")) {
					hasMin = true;
					minValue = mask;
				}
				
				if (mask.startsWith("V") || mask.startsWith("L")) {
					isExact = true;
					isDataVal = true;
					exactValue = mask.substring(1);
				}
			}
			
			if (isDataVal) {
				formattedValue = "V,";
			} else {
				formattedValue = "L,";
			}
			
			if (hasMin && hasMax) {
				formattedValue += minValue + ":" + maxValue;
			} else if (hasMin) {
				formattedValue += minValue;
			} else if (hasMax) {
				formattedValue += maxValue;
			} else if (isExact) {
				formattedValue += exactValue;
			}
			
			return formattedValue;
		} else {
			return null;
		}
	}
	
	
}
