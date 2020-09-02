package com.appdeveloperblog.app.ws;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	
	Contact contact = new Contact(
		"Moon Lee",
		"https://moonlee242.me",
		"buddybuddymoon@gmail.com"
	);
			
	
	private ApiInfo apiEndInfo() {
	    return new ApiInfoBuilder().title("API Reference")
	            .description("User API RESTful Web Service Documentation")
	            .contact(contact)
	            .license("Apache 2.0")
	            .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0.html")
	            .version("0.0.1-SNAPSHOT")
	            .build();
	}

	@Bean
	public Docket apiDocket() {
		Docket docket = new Docket(DocumentationType.SWAGGER_2)
			.select()
			.apis(RequestHandlerSelectors.basePackage("com.appdeveloperblog.app.ws"))
			.paths(PathSelectors.any())
			.build().apiInfo(apiEndInfo());
		return docket;
		
		
	}
	
	
	
}
