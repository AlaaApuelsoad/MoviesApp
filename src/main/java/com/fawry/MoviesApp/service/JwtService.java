package com.fawry.MoviesApp.service;

import com.fawry.MoviesApp.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
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

@Service
public class JwtService {

    private static String SECRET_KEY ;
    private final UserRepository userRepository;

    @Value("${app.security.jwt.expiration}")
    private long TOKEN_EXPIRATION_TIME;
    @Value("${app.security.jwt.issuer}")
    private String APP_ISSUER;

    public JwtService(UserRepository userRepository) throws NoSuchAlgorithmException {
        SECRET_KEY = generateSecretKey();
        this.userRepository = userRepository;
    }

    private String generateSecretKey() throws NoSuchAlgorithmException {
        try {
            KeyGenerator kenGen = KeyGenerator.getInstance("HmacSHA256");
            kenGen.init(512);
            SecretKey secretKey = kenGen.generateKey();
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        }catch (NoSuchAlgorithmException e){
            throw new RuntimeException("HmacSHA256 algorithm not supported",e);
        }
    }

    public String generateToken(String username) {
        Map<String,String> claims = new HashMap<>();
        claims.put("username",username);
        claims.put("role",userRepository.findByUsernameOrEmail(username).orElseThrow().getRole().getRoleName());
        claims.put("iss",APP_ISSUER);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+TOKEN_EXPIRATION_TIME))
                .signWith(getKey(), SignatureAlgorithm.HS256).compact();
    }

    private Key getKey(){
        byte[] keyBytes = Base64.getDecoder().decode("znDT/f+VWZoWWu3EyPqpWrNK9RpE5xLcAw9f1VM1M6rHFVHs6CfbMEkQHI/mOm2Ivk+TpSoK9DF3gzmXG1SaTA==");
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build().parseClaimsJws(token).getBody();
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
        final String username = extractUserIdentifier(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        KeyGenerator generator = KeyGenerator.getInstance("HmacSHA256");
        generator.init(512);
        SecretKey secretKey = generator.generateKey();
        String base64Key = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        System.out.println(base64Key);
    }
}
