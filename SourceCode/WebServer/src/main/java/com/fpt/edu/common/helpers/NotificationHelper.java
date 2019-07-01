package com.fpt.edu.common.helpers;

import com.fpt.edu.constant.Constant;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class NotificationHelper {
	private String endPoint;

	@Autowired
	public NotificationHelper(@Value("${notification.service}") String endpoint) {
		this.endPoint = endpoint;
	}

	public void pushNotification(String email, String message, String type) throws UnirestException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("email", email);
		jsonObject.put("message", message);
		jsonObject.put("type", type);

		Unirest.post(this.endPoint)
			.header("accept", "application/json")
			.header("Content-Type", Constant.APPLICATION_JSON)
			.body(jsonObject.toString())
			.asJson();
	}


}
