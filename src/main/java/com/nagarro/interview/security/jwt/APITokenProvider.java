package com.nagarro.interview.security.jwt;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.nagarro.interview.security.services.UserPrinciple;

import java.util.Date;

@Component
public class APITokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(APITokenProvider.class);

    @Value("${app.token.secret}")
    private String jwtTokenSecret;

    @Value("${app.token.expiration}")
    private int jwtTokenExpiration;
    
    @Autowired
    InMemoryTokenValut tokenVault;

    public String generateJwtToken(Authentication authentication) {
        UserPrinciple userPrincipal = (UserPrinciple) authentication.getPrincipal();

        return Jwts.builder()
		                .setSubject((userPrincipal.getUsername()))
		                .setIssuedAt(new Date())
		                .setExpiration(new Date((new Date()).getTime() + jwtTokenExpiration*1000))
		                .signWith(SignatureAlgorithm.HS512, jwtTokenSecret)
		                .compact();
    }
    
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtTokenSecret).parseClaimsJws(authToken);
            String token= tokenVault.getTokenByUserName(getUserFromJwtSubject(authToken));
            if(token==null || !token.equals(authToken))
            {logger.error("Valdi JWT For Signed Out User-> Message: )");
            return false;}
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid token signature -> Message: {} ", e);
        } catch (MalformedJwtException e) {
            logger.error("Invalid token -> Message: {}", e);
        } catch (ExpiredJwtException e) {
            logger.error("Expired  token -> Message: {}", e);
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported  token -> Message: {}", e);
        } catch (IllegalArgumentException e) {
            logger.error(" claims string is empty -> Message: {}", e);
        }
        
        return false;
    }

    public String getUserFromJwtSubject(String token) {
        return Jwts.parser()
                .setSigningKey(jwtTokenSecret)
                .parseClaimsJws(token)
                .getBody().getSubject();
    }
}