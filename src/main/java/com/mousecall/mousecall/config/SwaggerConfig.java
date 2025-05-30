package com.mousecall.mousecall.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(title = "MouseCall API", version = "v1", description = "민원 처리 시스템 백엔드 API 문서")
)
@SecurityScheme(
        name = "JWT",       // 이게 컨트롤러 위에 붙는 name과 일치하게해야함
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class SwaggerConfig {
}
