package com.nagarro.interview.api.error;


/**
 * ConcurrentLoginException.java This is a model class represents ConcurrentLoginException 
 * This Exception must only thrown when specific user tries to /login while there's still an active
 * session for the same user
 * 
 * @author Abdalhafeez Bushara
 *
 */
public class ConcurrentLoginException extends RuntimeException {
	private static final long serialVersionUID = -4013802444219982838L;

	public ConcurrentLoginException( String user) {
        super(ConcurrentLoginException.generateMessage(user));
    }

    private static String generateMessage(String entity) {
        return entity +" can not excute concurrent logins " ;
    }
}