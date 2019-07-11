package com.fpt.edu.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOrigins(
				"http://localhost:3000", "https://library.fptu.tech", "https://thuvienfu.com",
				"http://dev.fptu.tech", "https://dev.fptu.tech", "http://dev.fptu.tech:3000")
			.allowedMethods("GET", "POST", "PUT", "DELETE").exposedHeaders("Authorization")
			.allowCredentials(true);
	}
}
