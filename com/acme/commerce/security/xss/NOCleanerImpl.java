package com.acme.commerce.security.xss;

import org.owasp.esapi.errors.EncodingException;
import org.owasp.esapi.errors.IntrusionException;

/**
 * @author cgordon
 *
 */
public class NOCleanerImpl implements CleanerInterface {

	public String clean(String value) throws EncodingException, IntrusionException{
		return value;
	}
}
