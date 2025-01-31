package com.tfg.terranostra.utils;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "QffuHJPmBc4ZzcJLFV8gnoxdIdiBmaSwosu5epPy5ns";

    private final long EXPIRATION_TIME = 3600000;


    /**
     * Método que transforma la SECRET_KEY en formato para HS256;
     * @return Keys
     * @author ebp 31/1/25
     */
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    /**
     * Método que genera yn Token JWT
     * @param email
     * @return
     * @author ebp 31/1/25
     */
    public String generateToken(String email) {

        long ahora = System.currentTimeMillis();

        return Jwts.builder()
                .setSubject(e)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // 1 día de expiración
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }
}
