package io.cherrytechnologies.photoappzullapigateway.util

import org.springframework.core.env.Environment

fun getStringArrayFromEnvironment(env: Environment, property: String) =
    env.getProperty("open.urls")
        ?.split(",")
        ?.map { it.replace("\\s".toRegex(), "") }
        ?.filterNot { it == "" }