package com.bvd.paymentswitch.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.AbstractMap;
import java.util.ArrayList;


import java.util.List;


public class ProtocolUtils {
	
	public static Charset APP_CHARSET = StandardCharsets.UTF_8;
	public static final ZoneId utcZone = ZoneId.of("UTC");
	public static final DateTimeFormatter tsFormatter= DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
	
	public static AbstractMap.SimpleEntry<String, String> parseXmlField(String field) {
		
		if (field == null || field.length() < 2) return null;
		
		String key =  field.substring(0,2);
		String value = field.substring(2);
		
		if (value == null || value.isEmpty()) return null;
		
		return new AbstractMap.SimpleEntry<String, String>(key, value);	
	}
	
	public static void createXmlField(String key, String value, List<String> fields) {
		if (value != null) {
			fields.add("<" + key + value + "/>");
		}
	}
	
	public static AbstractMap.SimpleEntry<String, String> parseToken(String field) {
		
		if (field == null) return null;
		
		int firstColon = field.indexOf(":");
		
		if (firstColon == -1) return null;
		
		String key = field.substring(0,firstColon);
		String value =  field.substring(firstColon+1);
		
		if (value == null || value.isEmpty()) return null;
		
		return new AbstractMap.SimpleEntry<String, String>(key, value);	
	}
	
	public static void createToken(String key, String value, List<String> tokens) {
		if (value != null) {
			tokens.add(key + ":" + value);
		}
	}
	
	
	
	public static BigDecimal getBigDecimal(String nstr, int scale) {
		if (nstr == null) return null;
		
		BigDecimal number = null;
		try {
			number = new BigDecimal(nstr).setScale(scale,RoundingMode.HALF_UP);
			//number = number.setScale(scale,RoundingMode.HALF_UP);
		} catch (Exception e) {
			return null;
		}
		
		return number;
	}
	
	public static Timestamp stringToTimestamp(String timestamp) {
		if (timestamp == null) return null;
		
		Timestamp ts = null;
		try {
			LocalDateTime dateTime = LocalDateTime.parse(timestamp, tsFormatter);
			ts = Timestamp.valueOf(dateTime);
		} catch (Exception e) {
			return null;
		}
		
		return ts;
	}
	
	public static String timestampToString(Timestamp ts) {
		if (ts == null) return null;
		
		String timestamp = null;
		try {
			LocalDateTime dateTime = ts.toLocalDateTime();
			timestamp = dateTime.format(tsFormatter);
		} catch (Exception e) {
			return null;
		}
		
		return timestamp;
	}
	
	public static Timestamp getUTCTimestamp() {
		ZonedDateTime ts = ZonedDateTime.now(utcZone);
		return Timestamp.valueOf(ts.toLocalDateTime());
	}
	
	public static Timestamp getUTCTimestampOffset(long daysToOffset) {
		ZonedDateTime ts = ZonedDateTime.now(utcZone);
		ts = ts.minus(daysToOffset, ChronoUnit.DAYS);
		return Timestamp.valueOf(ts.toLocalDateTime());
	}
	
	
	
	public static boolean checkDigit(String request) {
	
		int messageLength = request.length();
		
		int inDigit = Integer.parseInt(request.substring(messageLength - 3));  // last 3 chars are the int value 
		
		// int inDigit = getCodePoint(request.charAt(messageLength-1));   // should be the last character
		
		String cText = request.substring(0, messageLength-3);
		
		int nChkDigit = calculateCheckDigit(cText);
		
		//System.err.println("Recieved: " + inDigit + ", Calculated: " + nChkDigit);
		return (nChkDigit == inDigit);
	}
	
	public static int calculateCheckDigit(String cText) {
		int nChkDigit =  getCodePoint(cText.charAt(0));
		for (int i=1; i < cText.length(); i++) {
			int codepoint = getCodePoint(cText.charAt(i));
			//System.err.println("nChkDigit: " + nChkDigit + ", XOR With: " + cText.charAt(i) +  " (codepoint= " + codepoint + ")");
			nChkDigit = nChkDigit^codepoint;
			//System.out.println(nChkDigit);
		}
		
		if (nChkDigit < 32) { // == 0 < || nChkDigit == 2 || nChkDigit == 3 || nChkDigit == 21) {
			nChkDigit = 255 - nChkDigit;
		}
		
		return nChkDigit;
	}
	
	public static int getInteger(String binaryString) {
		try {
			return Integer.parseUnsignedInt(binaryString, 2);
		} catch (Exception e) {
			return 0;
		}
	}
	
	
	public static String getBinary(String iStr) {
		try {
			int i = Integer.parseInt(iStr);
			String bitMap = Integer.toBinaryString(i);
			
			while (bitMap.length() < 8) {
				bitMap = "0" + bitMap;
			}
			
			return bitMap;
		} catch (Exception e) {
			return "00000000";
		}
	}
	
	
	public static String encodeInteger(int i) {
		String s = String.valueOf(i);
		while (s.length() < 3) {
			s = "0" + s;
		}
		return s;
	}	
	
	public static String getCharacter(String binaryString) {
		int val = Integer.parseInt(binaryString, 2);
		byte[] bytes = ByteBuffer.allocate(Integer.SIZE/8).putInt(val).array();
		String s = new String(bytes,APP_CHARSET);
		return s.trim();
	}
	
	
	public static String getCharacter(int val) {
		byte[] bytes = ByteBuffer.allocate(Integer.SIZE/8).putInt(val).array();
		String s = new String(bytes,APP_CHARSET);
		return s.trim();
	}
	
	public static int getCodePoint(char c) {
		BigInteger cp = new BigInteger(String.valueOf(c).getBytes(APP_CHARSET));
		
		int codepoint = cp.intValue();
		if (codepoint < 0) {
			codepoint += 256;		// fun times with BigIntegers... seems to give negative offset int value (from 256) for chars beyond the 128 code point range 
		}
		
		return codepoint;
	}

	public static String formatErrors(String errorCode) {
		// CODE:017,TEXT:INVALID UNIT ENTRY/CODE:017,TEXT:INVALID DRID ENTRY
		
		if (errorCode == null) return null;
		List<String> codes = new ArrayList<String>();
		String[] errors = errorCode.split("/");
		
		for (String e: errors) {
			String[] codetext = e.split(",");
			AbstractMap.SimpleEntry<String, String> codevalue = parseToken(codetext[0]);
			String value = codevalue.getValue();
			if (!codes.contains(value)) codes.add(value);
		}
		
		String x = ""; 
		
		for (String s : codes) {
			x += s + ",";
		}
		return x.substring(0, x.length()-1);
	}
	
	
	public static String finalizePrePostRequest(String msg, int lengthModifier) {
		// determine length of message
		int length = msg.length() + 6  + lengthModifier;   // the 6 extra are for the 4 bytes of length, the STX and ETX.  The extra length modifier is provider-specific
		String lstr = String.valueOf(length);
		while (lstr.length() < 4) {
			lstr = "0" + lstr;
		}
		
		return (lstr + msg);
	}

	
}
