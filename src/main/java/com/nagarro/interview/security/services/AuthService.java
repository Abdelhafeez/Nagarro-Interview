package com.nagarro.interview.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.nagarro.interview.security.jwt.APITokenProvider;



@Service
public class AuthService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    APITokenProvider jwtProvider;



    public Authentication authenticate(String userName, String password){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userName,
                        password
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    public String generateJwtToken(Authentication authentication){
       return jwtProvider.generateJwtToken(authentication);
    }
}