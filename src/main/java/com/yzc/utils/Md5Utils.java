package com.yzc.utils;

import java.security.MessageDigest;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import org.apache.log4j.Logger;

public class Md5Utils {

	private final static Logger logger = Logger.getLogger(Md5Utils.class);

	private final static String KEY_MD5 = "MD5";

	/**
	 * MD5+Salt加密
	 *
	 * @param content
	 * @return
	 */
	public static String encryptMD5_Salt(String content) {
		String resultString = "";
		String appkey = "fdjf,jkgfkl";

		byte[] a = appkey.getBytes();
		byte[] datSource = content.getBytes();
		byte[] b = new byte[a.length + 4 + datSource.length];

		int i;
		for (i = 0; i < datSource.length; i++) {
			b[i] = datSource[i];
		}

		b[i++] = (byte) 163;
		b[i++] = (byte) 172;
		b[i++] = (byte) 161;
		b[i++] = (byte) 163;

		for (int k = 0; k < a.length; k++) {
			b[i] = a[k];
			i++;
		}

		try {
			MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);
			md5.update(b);

			// 将得到的字节数组变成字符串返回
			resultString = new HexBinaryAdapter().marshal(md5.digest());// 转为十六进制的字符串
		} catch (Exception e) {
			logger.error("encryptMD5 fail", e);
		}

		return resultString.toLowerCase();
	}

	public static void main(String[] args) {
		System.out.println(encryptMD5_Salt("xu666121"));
		System.out.println(System.currentTimeMillis());
	}
}
