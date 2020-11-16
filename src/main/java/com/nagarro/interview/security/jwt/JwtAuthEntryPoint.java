package com.nagarro.interview.security.jwt;

//package com.nagroo.jwtauth.security.jwt;
//
//import static org.springframework.http.HttpStatus.UNAUTHORIZED;
//
//import java.io.IOException;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.WebRequest;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import com.nagroo.jwtauth.api.errors.ApiCommonError;
//
//@Component
//public class JwtAuthEntryPoint implements AuthenticationEntryPoint {
//
//    private static final Logger logger = LoggerFactory.getLogger(JwtAuthEntryPoint.class);
//    
//    @Override
//    public void commence(HttpServletRequest request,
//                         HttpServletResponse response,
//                         AuthenticationException e) 
//                        		 throws IOException, ServletException {
//    	
//        logger.error("Unauthorized error. Message - {}", e.getMessage());
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.registerModule(new JavaTimeModule());
//        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//        ApiCommonError ApiCommonError = new ApiCommonError(UNAUTHORIZED);
//        ApiCommonError.setMessage("Invalid Credentials");
//        response.getWriter().write(mapper.writeValueAsString(ApiCommonError));
//        response.setStatus(401);
//        response.setContentType("application/json");
//        
//    }
//}