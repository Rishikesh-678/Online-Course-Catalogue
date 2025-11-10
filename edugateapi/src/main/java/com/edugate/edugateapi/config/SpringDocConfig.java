// package com.edugate.edugateapi.config;

// import io.swagger.v3.oas.models.Components;
// import io.swagger.v3.oas.models.OpenAPI;
// import io.swagger.v3.oas.models.security.SecurityRequirement;
// import io.swagger.v3.oas.models.security.SecurityScheme;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;

// @Configuration
// public class SpringDocConfig {

//     /**
//      * This method configures the OpenAPI (Swagger) documentation
//      * to add a global "Authorize" button for JWT Bearer Authentication.
//      */
//     @Bean
//     public OpenAPI customOpenAPI() {
//         // Define the security scheme (Bearer Auth)
//         final String securitySchemeName = "bearerAuth";
//         SecurityScheme securityScheme = new SecurityScheme()
//                 .name(securitySchemeName)
//                 .type(SecurityScheme.Type.HTTP)
//                 .scheme("bearer")
//                 .bearerFormat("JWT");

//         // Add the security scheme to the OpenAPI components
//         Components components = new Components()
//                 .addSecuritySchemes(securitySchemeName, securityScheme);

//         // Add a global security requirement
//         SecurityRequirement securityRequirement = new SecurityRequirement()
//                 .addList(securitySchemeName);

//         return new OpenAPI()
//                 .components(components)
//                 .addSecurityItem(securityRequirement);
//     }
// }
package com.edugate.edugateapi.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info; // <-- ADD THIS IMPORT
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        // Define the security scheme (Bearer Auth)
        final String securitySchemeName = "bearerAuth";
        SecurityScheme securityScheme = new SecurityScheme()
                .name(securitySchemeName)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");

        // Add the security scheme to the OpenAPI components
        Components components = new Components()
                .addSecuritySchemes(securitySchemeName, securityScheme);

        // Add a global security requirement
        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList(securitySchemeName);
        
        // --- ADD THIS INFO OBJECT ---
        Info info = new Info()
                .title("EduGate API")
                .version("1.0.0")
                .description("API for the EduGate Online Course Catalog application.");
        // -----------------------------

        return new OpenAPI()
                .info(info) // <-- SET THE INFO HERE
                .components(components)
                .addSecurityItem(securityRequirement);
    }
}