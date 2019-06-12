package com.fpt.edu.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.fpt.edu.common.RequestQueueSimulate.Observer;
import com.fpt.edu.common.RequestQueueSimulate.PublishSubscribe;
import com.fpt.edu.common.RequestQueueSimulate.RequestQueueManager;
import com.fpt.edu.services.RequestServices;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class Config {
	protected final Logger LOGGER = LogManager.getLogger(getClass());
	@Autowired
	RequestQueueManager requestQueueManager;
	@Autowired
	RequestServices requestServices;
	@Value("${aws.accesskey}")
	private String accessKey;

	@Value("${aws.secretkey}")
	private String secretKey;
	@Value("${aws.s3.bucket.name}")
	private String bucketName;

	@Bean
	PublishSubscribe getPublisher() {

		List<Observer> list = new ArrayList<Observer>();
		list.add(requestServices);
		list.add(requestQueueManager);
		return new PublishSubscribe(list);


	}

	@Bean
	public AmazonS3 s3client() {
		AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
		AmazonS3 s3client = AmazonS3ClientBuilder
			.standard()
			.withCredentials(new AWSStaticCredentialsProvider(credentials))
			.withRegion(Regions.AP_SOUTHEAST_1)
			.build();
		if (!s3client.doesBucketExistV2(bucketName)) {
			LOGGER.info("Bucket name is not available. Create bucket name "+bucketName);
			s3client.createBucket(bucketName);
		}

		return s3client;
	}


}
