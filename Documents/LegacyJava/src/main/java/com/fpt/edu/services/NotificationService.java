package com.fpt.edu.services;

import com.fpt.edu.constant.Constant;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
	private final Logger LOGGER = LogManager.getLogger(getClass());
	private String endPoint;

	@Autowired
	public NotificationService(@Value("${notification.service}") String endpoint) {
		this.endPoint = endpoint;
	}

	public void pushNotification(String email, String message, String type) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("email", email);
		jsonObject.put("message", message);
		jsonObject.put("type", type);

		HttpResponse<JsonNode> response = null;
		try {
			response = Unirest.post(this.endPoint)
				.header("accept", "application/json")
				.header("Content-Type", Constant.APPLICATION_JSON)
				.body(jsonObject.toString())
				.asJson();
			LOGGER.info("Response from notification service: " + response.toString());
		} catch (UnirestException e) {
			LOGGER.error("Cannot send request to the endpoint: " + endPoint);
		}
	}
}
