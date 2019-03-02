package com.acme.commerce.security.xss;

import org.owasp.esapi.errors.EncodingException;
import org.owasp.esapi.errors.IntrusionException;

/**
 * @author cgordon
 *
 */
public class ECXCleanerImpl extends AbstractEvilCharCleanerImpl {

	public String cleanEvilChars(String value) throws EncodingException, IntrusionException{
		return super.cleanEvilChars(value, CleanerInterface.EXCEPT_FORM_TYPE);
	}
}
