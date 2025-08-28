package com.fawry.MoviesApp.filters;

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

    public JwtService(UserRepository userRepository) throws NoSuchAlgorithmException {
        SECRET_KEY = generateSecretKey();
        this.userRepository = userRepository;
    }

    private String generateSecretKey() throws NoSuchAlgorithmException {
        try {
            KeyGenerator kenGen = KeyGenerator.getInstance("HmacSHA256");
            SecretKey secretKey = kenGen.generateKey();
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        }catch (NoSuchAlgorithmException e){
            throw new RuntimeException("HmacSHA256 algorithm not supported",e);
        }
    }

    public String generateToken(String userIdentifier) {
        Map<String,String> claims = new HashMap<>();
        claims.put("role",userRepository.findByUsernameOrEmail(userIdentifier).orElseThrow().getRole().getRoleName());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userIdentifier)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+TOKEN_EXPIRATION_TIME))
                .signWith(getKey(), SignatureAlgorithm.HS256).compact();
    }

    private Key getKey(){
        byte[] keyBytes = Base64.getDecoder().decode(SECRET_KEY);
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
}
