package com.addnumbers.config;

import springfox.documentation.service.ApiInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Configuration
@EnableSwagger2
public class SwaggerConfiguration {// extends WebMvcConfigurerAdapter {
    @Bean
    public Docket api() {
      return new Docket(DocumentationType.SWAGGER_2)
              .select()
              .paths(PathSelectors.ant("/api/*"))
              .apis(RequestHandlerSelectors.basePackage("com.addnumbers"))
              .build()
              .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo(){
        return new ApiInfo("Smart Equip POC",
                    "To add the given numbers and checking the correctness of the sum",
                        "1.0",
                "/api/*", "yreddy", "license", "licenseUrl");
        }
}

