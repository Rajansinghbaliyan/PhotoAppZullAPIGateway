package io.cherrytechnologies.photoappzullapigateway

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.cloud.netflix.zuul.EnableZuulProxy

@SpringBootApplication
@EnableEurekaClient
@EnableZuulProxy
class PhotoAppZullApiGatewayApplication

fun main(args: Array<String>) {
    runApplication<PhotoAppZullApiGatewayApplication>(*args)
}
