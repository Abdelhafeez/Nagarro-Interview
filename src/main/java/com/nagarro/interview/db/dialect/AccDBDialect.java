package com.nagarro.interview.db.dialect;

import org.hibernate.dialect.SQLServerDialect;

public class AccDBDialect extends SQLServerDialect {
	
	 @Override
	    public char openQuote() {
	        return '[';
	    }
	    
	    @Override
	    public char closeQuote() {
	        return ']';
	    }

}
