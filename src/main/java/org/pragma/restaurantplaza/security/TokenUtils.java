package org.pragma.restaurantplaza.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


import javax.crypto.SecretKey;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;





public class TokenUtils {

    private static  final  String ACCESS_TOKEN_SECRET = "4g6sd45g6sd4g98d";
    private static  final  Long ACCESS_TOKEN_VALIDITY_SECONDS = 3600000L;

    private TokenUtils() {
    }

    public static String createToken(String nombre, String email) {
        long expirationTime = ACCESS_TOKEN_VALIDITY_SECONDS * 1_000;
        Date expitrationDate = new Date(System.currentTimeMillis() + expirationTime);

        Map<String, Object> extra = new HashMap<>();
        extra.put("nombre", nombre);

        SecretKey key = Keys.hmacShaKeyFor(ACCESS_TOKEN_SECRET.getBytes());

        return Jwts.builder()
                .setClaims(extra)
                .setSubject(email)
                .setExpiration(expitrationDate)
                .signWith(key,SignatureAlgorithm.HS512)
                .compact();
    }

    public static UsernamePasswordAuthenticationToken getAuthentication(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(ACCESS_TOKEN_SECRET.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String email = claims.getSubject();

            return new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());


        } catch (JwtException e) {
            return null;
        }

    }
}
