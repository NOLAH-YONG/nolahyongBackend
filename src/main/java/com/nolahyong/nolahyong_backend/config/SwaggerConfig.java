package com.nolahyong.nolahyong_backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.ExternalDocumentation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Nolahyong Backend API")
                        .description("Nolahyong 서비스의 백엔드 REST API 문서입니다.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("API Support")
                                .url("https://nolahyong.com/contact")
                                .email("support@nolahyong.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html"))
                )
                .servers(List.of(
                        new Server().url("https://api.nolahyong.com").description("Production"),
                        new Server().url("http://localhost:8080").description("Local Development")
                ))
                .externalDocs(new ExternalDocumentation()
                        .description("전체 문서")
                        .url("https://nolahyong.com/docs"));
    }
}
