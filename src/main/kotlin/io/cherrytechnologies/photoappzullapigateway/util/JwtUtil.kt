package io.cherrytechnologies.photoappzullapigateway.util

import io.cherrytechnologies.photoappzullapigateway.customexception.JwtTokenExpired
import io.cherrytechnologies.photoappzullapigateway.customexception.JwtTokenMalformedException
import io.cherrytechnologies.photoappzullapigateway.customexception.JwtTokenMissingException
import io.jsonwebtoken.*
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import java.util.*
import java.util.logging.Logger


@Configuration
class JwtUtil(env: Environment) {

    val log:Logger = Logger.getLogger(JwtUtil::class.toString())

    var jwtSecret: String? = env.getProperty("jwt.secret")
    var jwtExpiration: Long? = env.getProperty("jwt.expiration")?.toLong()

    fun getClaims(token: String) = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).body

    @Throws(JwtTokenMalformedException::class, JwtTokenMissingException::class)
    fun validateToken(token: String?) {
        try {
            val claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).body
             if (claims.expiration.before(Date())) throw JwtTokenExpired("expired")
        } catch (ex: SignatureException) {
            throw JwtTokenMalformedException("Invalid JWT signature")
        } catch (ex: MalformedJwtException) {
            throw JwtTokenMalformedException("Invalid JWT token")
        } catch (ex: ExpiredJwtException) {
            throw JwtTokenMalformedException("Expired JWT token")
        } catch (ex: UnsupportedJwtException) {
            throw JwtTokenMalformedException("Unsupported JWT token")
        } catch (ex: IllegalArgumentException) {
            throw JwtTokenMissingException("JWT claims string is empty.")
        }
        catch (e: Exception) {
            log.log(java.util.logging.Level.SEVERE,e.message)
        }
    }

}