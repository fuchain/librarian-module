package com.fpt.edu.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	 @Bean
	 public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).host("lapi.fptu.tech")
		  .select()
		  .apis(RequestHandlerSelectors.basePackage("com.fpt.edu.controllers"))
		  .paths(PathSelectors.any())
		  .build().apiInfo(new ApiInfoBuilder().version("1.0").title("FPT Library API").description("Documentation Library API v1.0").build())	;
	 }

	 @Bean
	public ApiInfo apiInfo() {
		final ApiInfoBuilder builder = new ApiInfoBuilder();
		builder.title("FPT Library Module").version("0.1").license("(C) Copyright")
		.description("This Document describe about the API that library module export for consumer");

		return builder.build();
	}
}
