package util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class SecureUtil {
	public static String generateSHA256Hash(String input) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
			byte[] encodedHash = messageDigest.digest(input.getBytes(StandardCharsets.UTF_8));
			return bytesToHex(encodedHash);
		}catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
	
	private static String bytesToHex(byte[] hash) {
	    StringBuffer hexString = new StringBuffer();
	    for (int i = 0; i < hash.length; i++) {
	    String hex = Integer.toHexString(0xff & hash[i]);
	    if(hex.length() == 1) hexString.append('0');
	        hexString.append(hex);
	    }
	    return hexString.toString();
	}
}
