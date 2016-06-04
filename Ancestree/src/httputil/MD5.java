package httputil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {

	public static String getMD5(String val) throws NoSuchAlgorithmException {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(val.getBytes());
		byte[] m = md5.digest();
		StringBuffer sb = new StringBuffer();
		for (byte b : m) {
			sb.append(String.format("%02x", b & 0xff));
		}
		return sb.toString();
	}
}
