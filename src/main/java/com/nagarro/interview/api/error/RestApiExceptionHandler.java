package com.nagarro.interview.api.error;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;


@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j
public class RestApiExceptionHandler extends ResponseEntityExceptionHandler {
//	@Autowired
//	@Qualifier(value="validationMessageSource")
//    private MessageSource messageSource;

    /**
     * RestApiExceptionHandler Handle MissingServletRequestParameterException.
     *
     * @param e      MissingServletRequestParameterException
     * @param headers headers
     * @param status  status
     * @param request request
     * @return the ApiCommonError object
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException e, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        String error = e.getParameterName()  +" some parameter is missing";
        return buildResponseEntity(new Error(BAD_REQUEST, error, e));
    }
    
    


    /**
     * Handle HttpMediaTypeNotSupportedException. This one triggers when JSON is invalid as well.
     *
     * @param e      HttpMediaTypeNotSupportedException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ApiCommonError object
     */
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException e,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(e.getContentType());
        builder.append("Type is not supported, supported are");
        e.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));
        return buildResponseEntity(new Error(HttpStatus.UNSUPPORTED_MEDIA_TYPE, builder.substring(0, builder.length() - 2), e));
    }

    /**
     * Handle MethodArgumentNotValidException. Triggered when an object fails @Valid validation.
     *
     * @param e      the MethodArgumentNotValidException that is thrown when @Valid validation fails
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ApiCommonError object
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        Error ApiCommonError = new Error(BAD_REQUEST);
        ApiCommonError.setMessage("Validation Error");
        ApiCommonError.addValidationErrors(e.getBindingResult().getFieldErrors());
        ApiCommonError.addValidationError(e.getBindingResult().getGlobalErrors());
        return buildResponseEntity(ApiCommonError);
    }

    /**
     * Handle HttpMessageNotReadableException. Happens when request JSON is malformed.
     *
     * @param e      HttpMessageNotReadableException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ApiCommonError object
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException e, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ServletWebRequest servletWebRequest = (ServletWebRequest) request;
        log.info("{} to {}", servletWebRequest.getHttpMethod(), servletWebRequest.getRequest().getServletPath());
        String error = ("Invalid Format");
        e.printStackTrace();
        return buildResponseEntity(new Error(HttpStatus.BAD_REQUEST, error, e));
    }

    /**
     * Handle HttpMessageNotWritableException.
     *
     * @param e      HttpMessageNotWritableException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ApiCommonError object
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException e, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "Error writing JSON output";
        return buildResponseEntity(new Error(HttpStatus.INTERNAL_SERVER_ERROR, error, e));
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException e, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Error ApiCommonError = new Error(BAD_REQUEST);
        ApiCommonError.setMessage(String.format("Could not find the %s method for URL %s", e.getHttpMethod(), e.getRequestURL()));
        return buildResponseEntity(ApiCommonError);
    }

    /**
     * Handles javax.validation.ConstraintViolationException. Thrown when @Validated fails.
     *
     * @param e the ConstraintViolationException
     * @return the ApiCommonError object
     */
    @ExceptionHandler(javax.validation.ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(
            javax.validation.ConstraintViolationException e) {
        Error ApiCommonError = new Error(BAD_REQUEST);
        ApiCommonError.setMessage("Validation Error");
        ApiCommonError.addValidationErrors(e.getConstraintViolations());
        return buildResponseEntity(ApiCommonError);
    }
    
    /**
     * Handles javax.validation.ConstraintViolationException. Thrown when @Validated fails.
     *
     * @param e the ConstraintViolationException
     * @return the ApiCommonError object
     */
    @ExceptionHandler(JwtException.class)
    protected ResponseEntity<Object> handleTokenViolation(
    		JwtException e) {
        Error ApiCommonError = new Error(UNAUTHORIZED);
        ApiCommonError.setMessage("Invalid Credentials");
       // ApiCommonError.addValidationErrors(null);
        return buildResponseEntity(ApiCommonError);
    }



    /**
     * Handle javax.persistence.EntityNotFoundException
     */
    @ExceptionHandler(javax.persistence.EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(javax.persistence.EntityNotFoundException e) {
        return buildResponseEntity(new Error(NOT_FOUND, e));
    }

    

    /**
     * Handle Exception, handle generic Exception.class
     *
     * @param e the Exception
     * @return the ApiCommonError object
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException e,
                                                                      WebRequest request) {
        Error ApiCommonError = new Error(BAD_REQUEST);
        ApiCommonError.setMessage(String.format("The parameter '%s' of value '%s' could not be converted to type '%s'", e.getName(), e.getValue(), e.getRequiredType().getSimpleName()));
        return buildResponseEntity(ApiCommonError);
    }
    
    /**
     * Handle Exception, handle  ConcurrentLoginException.class
     *
     * @param e the ConcurrentLoginException
     * @return the ApiCommonError object
     */
    @ExceptionHandler(ConcurrentLoginException.class)
    protected ResponseEntity<Object> handleConcurrentLoginException(ConcurrentLoginException e,
                                                                      WebRequest request) {
        Error ApiCommonError = new Error(BAD_REQUEST);
        ApiCommonError.setMessage(String.format(e.getMessage()));
        return buildResponseEntity(ApiCommonError);
    }
    
    /**
     * Handle Exception, handle  ConcurrentLoginException.class
     *
     * @param e the ConcurrentLoginException
     * @return the ApiCommonError object
     */
    @ExceptionHandler(UserCanNotFilterParametersException.class)
    protected ResponseEntity<Object> handleAdminCanNotFilterParametersException(UserCanNotFilterParametersException e,
                                                                      WebRequest request) {
        Error ApiCommonError = new Error(UNAUTHORIZED);
        ApiCommonError.setMessage(e.getMessage());
        return buildResponseEntity(ApiCommonError);
    }
    
    
    
    
    
    
 
    
    
    /**
     * Handle Exception, handle generic MethodNotAllowedException.class
     *
     * @param e the Exception
     * @return the ApiCommonError object
     */
    @ExceptionHandler(MethodNotAllowedException.class)
    protected ResponseEntity<Object> handleMethodNotAllowedException(MethodNotAllowedException e,
                                                                      WebRequest request) {
        Error ApiCommonError = new Error(METHOD_NOT_ALLOWED);
        ApiCommonError.setMessage(String.format(e.getMessage()));
        return buildResponseEntity(ApiCommonError);
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(AccessDeniedException e,
                                                                      WebRequest request) {
        Error ApiCommonError = new Error(UNAUTHORIZED);
        ApiCommonError.setMessage(e.getMessage());
        return buildResponseEntity(ApiCommonError);
    }

    private ResponseEntity<Object> buildResponseEntity(Error ApiCommonError) {
        return new ResponseEntity<>(ApiCommonError, ApiCommonError.getStatus());
    }

}