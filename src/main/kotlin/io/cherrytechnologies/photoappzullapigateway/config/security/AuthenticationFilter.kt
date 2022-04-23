package io.cherrytechnologies.photoappzullapigateway.config.security

import io.cherrytechnologies.photoappzullapigateway.util.JwtUtil
import io.cherrytechnologies.photoappzullapigateway.customexception.InternalServerException
import io.cherrytechnologies.photoappzullapigateway.util.getStringArrayFromEnvironment
import org.springframework.cloud.context.config.annotation.RefreshScope
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
@RefreshScope
class AuthenticationFilter(val jwtUtil: JwtUtil, val env: Environment) : GatewayFilter {
    override fun filter(exchange: ServerWebExchange?, chain: GatewayFilterChain?): Mono<Void> {
        try {
            val openEndpoints = getStringArrayFromEnvironment(env, "open.urls")
            val request = exchange?.request ?: throw InternalServerException("something went wrong")
            val isSecured = { r: ServerHttpRequest ->
                openEndpoints
                    ?.none { r.uri.path.contains(it) }
                    ?: true
            }

            if (isSecured(request)) {
                if (!request.headers.containsKey("Authorization"))
                    return returnBadRequest(exchange)
                else
                    performSecureOperations(exchange, request)
            }

            return chain?.filter(exchange) ?: throw InternalServerException("something went wrong")

        } catch (e: Exception) {
            val response = exchange?.response ?: throw InternalServerException("something went wrong")
            response.statusCode = HttpStatus.BAD_REQUEST
            return response.setComplete()
        }
    }

    fun returnBadRequest(exchange: ServerWebExchange): Mono<Void> {
        val response = exchange.response
        response.statusCode = HttpStatus.UNAUTHORIZED
        return response.setComplete()
    }

    fun performSecureOperations(exchange: ServerWebExchange, request: ServerHttpRequest) {
        val token = request.headers.getOrEmpty("Authorization")[0]
        jwtUtil.validateToken(token)
        val claims = jwtUtil.getClaims(token)
        exchange.request.mutate()
            .header("user-id", claims.id)
            .header("user-email", claims.subject)
            .build()
    }

}
