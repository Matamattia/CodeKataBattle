package com.codekatabattle.codebattle.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtUtils {

    private final String jwtSecret = "your_jwt_secret"; // Sostituisci con la tua chiave segreta
    private final long jwtExpirationMs = 86400000; // Imposta la durata del token (es. 24 ore)

    public String generateToken(String userEmail) {
        return Jwts.builder()
                .setSubject(userEmail)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    // Qui puoi aggiungere altri metodi, come per convalidare il token o estrarre informazioni da esso
}
