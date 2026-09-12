package com.alaa.MoviesApp.service;

import com.alaa.MoviesApp.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Service
public class JwtService {

    private final UserRepository userRepository;

    @Value("${app.security.jwt.expiration}")
    private long tokenExpirationTime;
    @Value("${app.security.jwt.issuer}")
    private String appIssuer;

    public JwtService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String generateToken(String username) {
        Map<String,String> claims = new HashMap<>();
        claims.put("username",username);
        claims.put("role",userRepository.findByUsernameOrEmail(username).orElseThrow().getRole().getRoleName());

        return Jwts.builder()
                .issuer(appIssuer)
                .claims(claims)
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + tokenExpirationTime))
                .signWith(getKey())
                .compact();
    }

    private Key getKey(){
        byte[] keyBytes = Base64.getDecoder().decode("znDT/f+VWZoWWu3EyPqpWrNK9RpE5xLcAw9f1VM1M6rHFVHs6CfbMEkQHI/mOm2Ivk+TpSoK9DF3gzmXG1SaTA==");
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getKey())
                .build()
                .parseSignedClaims(token).getPayload();
    }

    private <T> T extractClaims(String token, Function<Claims,T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractUserIdentifier(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    private Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUserIdentifier(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        KeyGenerator generator = KeyGenerator.getInstance("HmacSHA256");
        generator.init(512);
        SecretKey secretKey = generator.generateKey();
        String base64Key = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        log.info("Generated Secret Key: {}", base64Key);
    }
}
