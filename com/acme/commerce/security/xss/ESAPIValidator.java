package com.acme.commerce.security.xss;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Validator;

/**
 * @author cgordon
 *
 */
public class ESAPIValidator {
	private static Validator validator = null;
	
	public ESAPIValidator(){
	}
	
	public static Validator getValidator(){
		if(validator==null){
			validator = ESAPI.validator();
		}
		return validator;
	}
}
