package com.nagarro.interview.api.error;


/**
 * UserCanNotFilterParametersException.java This is a model class represents UserCanNotFilterParametersException 
 * This Exception must only thrown when specific user tries to add filter data
 * 
 * @author Abdalhafeez Bushara
 *
 */
public class UserCanNotFilterParametersException extends RuntimeException {
	private static final long serialVersionUID = -401380233219982838L;

	public UserCanNotFilterParametersException( String user) {
        super(UserCanNotFilterParametersException.generateMessage(user));
    }

    private static String generateMessage(String entity) {
        return entity +" can not excute filter by any amount or date parameters " ;
    }
}