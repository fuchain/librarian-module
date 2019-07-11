package com.fpt.edu.services;

import com.fpt.edu.constant.Constant;
import com.mashape.unirest.http.Unirest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LoggingService {

	private final Logger LOGGER = LogManager.getLogger(getClass());
	private final String URL;
	private final String ENV;

	@Autowired
	public LoggingService(
		@Value("${logging.service.host}") String loggingServiceHost,
		@Value("${server.servlet.context-path}") String contextPrefix
	) {
		this.URL = loggingServiceHost + contextPrefix + "/logs";
		String env = System.getenv("SPRING_PROFILES_ACTIVE");
		this.ENV = env == null ? "local" : env;
	}

	public void logError(Exception ex) {
		if (ENV.equals("prod")) {
			try {
				JSONObject errObj = new JSONObject();
				errObj.put("type", "error");
				errObj.put("source", "webserver");
				errObj.put("metadata", ex.toString());
				Unirest.post(this.URL)
					.header("accept", Constant.APPLICATION_JSON)
					.header("Content-Type", Constant.APPLICATION_JSON)
					.body(errObj.toString())
					.asJson();
			} catch (Exception e) {
				LOGGER.error("Logging failed to logging service host!!!");
			}
		}
	}
}
