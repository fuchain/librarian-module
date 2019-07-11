package com.fpt.edu.common.helpers;

import org.springframework.core.io.InputStreamResource;

import java.security.MessageDigest;

public class ImageHelper {

	private static final String SHA_256_HASH = "SHA-256";

	public static String hashFromUrl(InputStreamResource inputStreamResource) throws Exception {
		StringBuilder stringBuilder = new StringBuilder();
		byte[] data = inputStreamResource.getInputStream().readAllBytes();
		MessageDigest md5Digest = MessageDigest.getInstance(SHA_256_HASH);

		byte[] hashBytes = md5Digest.digest(data);
		for (byte hashByte : hashBytes) {
			stringBuilder.append(String.format("%x", hashByte));
		}

		return stringBuilder.toString();
	}
}
