package io.cherrytechnologies.photoappzullapigateway

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient

@SpringBootApplication
@EnableEurekaClient
class PhotoAppZullApiGatewayApplication

fun main(args: Array<String>) {
    runApplication<PhotoAppZullApiGatewayApplication>(*args)
}
