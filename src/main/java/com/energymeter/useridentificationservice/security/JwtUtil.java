package com.energymeter.useridentificationservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Component
public class JwtUtil {

    private static final Key SECRET_KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode("sampleAppSecretKeyWhichMayNotBeSecureEnough"));

    public static boolean isTokenExpired(String jwt) {
        return extractExpiration(jwt).before(new Date());
    }

    public static Date extractExpiration(String jwt) {
        return extractClaims(jwt, Claims::getExpiration);
    }

    private static <T> T extractClaims(String jwt, Function<Claims, T> claimResolver) {
        var claims = Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(jwt).getBody();
        return claimResolver.apply(claims);
    }

    public static String extractUsername(String jwt) {
        return extractClaims(jwt, Claims::getSubject);
    }

    public static String generateToken(UserDetails userDetails) {
        var claims = generateClaims(userDetails);
        return generateToken(claims, userDetails.getUsername());
    }

    private static Map generateClaims(UserDetails userDetails) {
        var claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities());
        claims.put("userId", ((CustomUserDetails) userDetails).getUserId());
        return claims;
    }

    private static String generateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .addClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
                .signWith(SECRET_KEY)
                .compact();
    }

    public static boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(SECRET_KEY)
                    .build().parseClaimsJws(token);
            return !isTokenExpired(token);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String extractRole(String jwt) {
        var claims = Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(jwt).getBody();
        var roles = (List<LinkedHashMap<String, String>>) claims.get("roles");
        return roles.get(0).get("authority");
    }

    public static String extractUserId(String jwt) {
        return (extractClaims(jwt, claims -> claims.get("userId"))).toString();
    }

}
