package com.fpt.edu.common.helper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.security.MessageDigest;

public class ImageHelper {

	private static final String SHA_256_HASH = "SHA-256";

	public static String hashFromUrl(String imgUrl) throws Exception {
		StringBuilder stringBuilder = new StringBuilder();
		BufferedImage bufferedImage = ImageIO.read(new URL(imgUrl));

		String imgType = imgUrl
			.substring(imgUrl.length() - 3, imgUrl.length())
			.toLowerCase();

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ImageIO.write(bufferedImage, imgType, outputStream);
		byte[] data = outputStream.toByteArray();

		MessageDigest md5Digest = MessageDigest.getInstance(SHA_256_HASH);
		byte[] hashBytes = md5Digest.digest(data);
		for (byte hashByte : hashBytes){
			stringBuilder.append(String.format("%x", hashByte));
		}

		return stringBuilder.toString();
	}
}
