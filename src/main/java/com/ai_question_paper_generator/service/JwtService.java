package com.ai_question_paper_generator.service;

import com.ai_question_paper_generator.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;



    public String generateToken(User user){
        return Jwts.builder()
                .subject(user.getEmail())
                .claim("name", user.getName())
                .claim("provider", user.getProvider())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+expiration))
                .signWith(getSigningKey())
                .compact();
    }


    public boolean isTokenValid(String token){
        try{
            getClaims(token);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }
                                                                                                                                                                                                                                                                                                                                        //This is developed by  Mohammad Zubair (zubair82187@gmail.com)
    public String extractEmail(String token){
        return getClaims(token).getSubject();
    }

    private SecretKey getSigningKey(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    private Claims getClaims(String token){
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
