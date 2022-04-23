package io.cherrytechnologies.photoappzullapigateway.config.gateway

import io.cherrytechnologies.photoappzullapigateway.config.security.AuthenticationFilter
import org.springframework.cloud.gateway.route.builder.PredicateSpec
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RouteValidator(val authenticationFilter: AuthenticationFilter) {

    @Bean
    fun routes(builder: RouteLocatorBuilder) =
        builder.routes()
            .route(
                "authentication-api",
                returnRouteLambda("/auth/**", "lb://authentication-api")
            )
            .route(
                "user-api",
                returnRouteLambda("/users/**", "lb://user-api")
            )
            .route(
                "account-management-api",
                returnRouteLambda("/account-management-api/**", "lb://account-management-api")
            )
            .build()

    fun returnRouteLambda(patterns: String, uri: String) = { r: PredicateSpec ->
        r.path(patterns)
            .filters { f -> f.filter(authenticationFilter) }
            .uri(uri)
    }
}