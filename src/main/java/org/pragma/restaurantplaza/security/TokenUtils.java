package org.pragma.restaurantplaza.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
@AllArgsConstructor
public class TokenUtils {

    static private final  String ACCESS_TOKEN_SECRET = "4g6sd45g6sd4g98d";
    static private final  Long ACCESS_TOKEN_VALIDITY_SECONDS = 3600000L;


    public static String createToken(String nombre, String email) {
        long expirationTime = ACCESS_TOKEN_VALIDITY_SECONDS * 1_000;
        Date expitrationDate = new Date(System.currentTimeMillis() + expirationTime);

        Map<String, Object> extra = new HashMap<>();
        extra.put("nombre", nombre);

        return Jwts.builder()
                .setClaims(extra)
                .setSubject(email)
                .setExpiration(expitrationDate)
                .signWith(SignatureAlgorithm.HS512, ACCESS_TOKEN_SECRET)
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
