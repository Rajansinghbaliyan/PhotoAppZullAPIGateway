package io.cherrytechnologies.photoappzullapigateway.config.gateway

import io.cherrytechnologies.photoappzullapigateway.config.security.AuthenticationFilter
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RouteValidator(val authenticationFilter: AuthenticationFilter) {

    @Bean
    fun routes(builder: RouteLocatorBuilder) =
        builder.routes()
            .route(
                "authentication-api"
            ) { r ->
                r.path("/auth/**")
                    .filters { f -> f.filter(authenticationFilter) }
                    .uri("lb://authentication-api")
            }
            .route(
                "user-api"
            ) { r ->
                r.path("/users/**")
                    .filters { f -> f.filter(authenticationFilter) }
                    .uri("lb://user-api")
            }
            .route(
                "account-management-api"
            ) { r ->
                r.path("/account-management-api/**")
                    .filters { f -> f.filter(authenticationFilter) }
                    .uri("lb://account-management-api")
            }
            .build()
}