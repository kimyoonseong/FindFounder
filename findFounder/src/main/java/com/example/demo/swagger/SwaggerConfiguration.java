package com.example.demo.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.RequiredArgsConstructor;

@Configuration
public class SwaggerConfiguration implements WebMvcConfigurer{
	@Bean
	public OpenAPI openAPI() {
		Info info = new Info()
				.title("swagger 테스트")
				.version("1.0")
				.description("API에 대한 설명 부분");
		return new OpenAPI()
				.components(new Components())
				.info(info);
	}
}