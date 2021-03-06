package com.nagarro.interview.security.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.nagarro.interview.security.services.UserDetailsServiceImpl;

public class AuthenticationTokenFilter extends OncePerRequestFilter {

	@Autowired
	private APITokenProvider tokenProvider;

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	private static final Logger logger = LoggerFactory.getLogger(AuthenticationTokenFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {

			String token = getToken(request);
			if (token != null && tokenProvider.validateToken(token)) {
				String userName = tokenProvider.getUserFromJwtSubject(token);
				UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		} catch (Exception e) {
			logger.error("Can not set user authentication / failed login -> Message: {}", e);

		}

		filterChain.doFilter(request, response);
	}

	private String getToken(HttpServletRequest request) {
		String authenticationHeader = request.getHeader("Authorization");
		if (authenticationHeader != null && authenticationHeader.startsWith("Bearer ")) {
			return authenticationHeader.replace("Bearer ", "");
		}

		return null;
	}
}
