package com.fpt.edu.common.helpers;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3URI;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fpt.edu.constant.Constant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Component

public class UtilHelper {
	private String bucketName;
	private AmazonS3 s3Client;
	private static final String SHA_256_HASH = "SHA-256";

	@Autowired
	public UtilHelper(AmazonS3 s3Client, @Value("${aws.s3.bucket.name}") String bucketName) {
		this.s3Client = s3Client;
		this.bucketName = bucketName;
	}

	protected final Logger LOGGER = LogManager.getLogger(getClass());

	public JSONObject buildListEntity(List<?> list, HttpServletRequest httpServletRequest) throws JsonProcessingException {
		JSONObject jsonObject = new JSONObject();
		JSONArray arr = new JSONArray();
		ObjectMapper objectMapper = new ObjectMapper();
		Hibernate5Module hbm = new Hibernate5Module();
		objectMapper.registerModule(hbm);
		jsonObject.put(Constant.ITEMS, arr);
		return jsonObject;
	}

	public JSONObject convertObjectToJSONObject(Object o) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = objectMapper.writeValueAsString(o);
		return new JSONObject(jsonString);
	}

	public String buildItemDetailLink(String currentPath, JSONObject raw) {
		String id = raw.getString(Constant.ID);
		String result = currentPath.replaceAll(Constant.REGULAR_ID_EXP, id);
		return result;
	}

	private String getValueOfAKey(JSONObject object, String keyName) {
		Iterator<?> it = object.keys();
		if (it.hasNext()) {
			String currentKeyname = (String) it.next();
			Object currentKeyValue = object.get(currentKeyname);
			if (currentKeyValue instanceof JSONObject) {
				getValueOfAKey((JSONObject) currentKeyValue, keyName);
			} else if (currentKeyValue instanceof JSONArray) {
				JSONArray arr = (JSONArray) currentKeyValue;
				for (int i = 0; i < arr.length(); i++) {
					getValueOfAKey(arr.getJSONObject(i), keyName);
				}
			}
			if (currentKeyValue instanceof String && currentKeyname.equals(keyName)) {
				return currentKeyValue.toString();
			}
		}
		return "";
	}

	// Build the root path for the server like http://localhost:9090/api/v1
	public String buildServerRootPath(HttpServletRequest httpServletRequest) {
		return
			httpServletRequest.getScheme() + "://" +
				httpServletRequest.getServerName() + ":" +
				httpServletRequest.getServerPort() +
				httpServletRequest.getContextPath();

	}

	// Get pin from random number
	public String getPin() {
		Random random = new Random();
		int number = random.nextInt(Constant.RANDOM_BOUND);

		return String.format("%06d", number);
	}

	// Get duration between 2 dates
	public static long getDuration(Date oldDate, Date newDate, TimeUnit timeUnit) {
		long diffInMillies = newDate.getTime() - oldDate.getTime();
		return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
	}


	public File convertMultiPartToFile(MultipartFile file) throws IOException {
		File convFile = new File(generateFileName(file));
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		return convFile;
	}

	private String generateFileName(MultipartFile multiPart) {
		return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
	}

	public String uploadFile(MultipartFile multipartFile) {
		String fileUrl = "";
		try {
			File file = convertMultiPartToFile(multipartFile);
			String fileName = generateFileName(multipartFile);
			fileUrl = "";
			fileUrl = uploadFileTos3bucket(fileName, file);
			file.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileUrl;
	}

	public String uploadFileTos3bucket(String fileName, File file) {
		s3Client.putObject(new PutObjectRequest(bucketName, fileName, file)
			.withCannedAcl(CannedAccessControlList.PublicRead));
		return s3Client.getUrl(this.bucketName, fileName).toString();

	}

	// not completed yet
	public InputStreamResource downloadFileTos3bucket(String fileUrl) throws URISyntaxException {
		URI fileToBeDownloaded = new URI(fileUrl);
		AmazonS3URI amazonS3URI = new AmazonS3URI(fileToBeDownloaded);
		S3Object s3Object = s3Client.getObject(amazonS3URI.getBucket(), amazonS3URI.getKey());
		S3ObjectInputStream inputStream = s3Object.getObjectContent();
		InputStreamResource resource = new InputStreamResource(inputStream.getDelegateStream());
		return resource;
	}

}
