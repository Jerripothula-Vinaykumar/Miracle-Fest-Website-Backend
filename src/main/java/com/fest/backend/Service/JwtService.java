package com.fest.backend.Service;

import com.fest.backend.Entity.FestUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.cglib.core.internal.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    private String secretKey = null;

    public String generateToken(FestUser user) {

        Map<String , Object> claims =
                new HashMap<>();

         return Jwts
                 .builder()
                 .claims()
                 .add(claims)
                 .subject(user.getEmail())
                 .issuer("Vinay Kumar J")
                 .issuedAt( new Date(System.currentTimeMillis()))
                 .expiration( new Date(System.currentTimeMillis() + ( 60 * 10 * 1000) ))
                 .and()
                 .signWith(generateKey())
                 .compact();

    }

    public SecretKey generateKey()
    {
        byte[] decode
                = Decoders.BASE64.decode(getSecretKey());
        return Keys.hmacShaKeyFor(decode);
    }

    public  String getSecretKey()
    {
        return secretKey =  "5f28be977cce381a8d5a3f3e7b6b426100987767757b5ba09c9bcfdb98212372";
    }

    public String extractEmail(String token) {

        return extractClaims(token , Claims::getSubject);
    }

    private <T> T extractClaims(String token, Function<Claims,T> claimsResolver) {
        Claims claims = extractClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractClaims(String token)
    {
       return Jwts
                .parser()
                .verifyWith(generateKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userEmail = extractEmail(token);

        return (userEmail.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {

        return  extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return  extractClaims(token , Claims::getExpiration);
    }
}
