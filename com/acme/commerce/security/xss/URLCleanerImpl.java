package com.acme.commerce.security.xss;

import org.owasp.esapi.errors.EncodingException;
import org.owasp.esapi.errors.IntrusionException;
/**
 * @author cgordon
 *
 */
public class URLCleanerImpl implements CleanerInterface {
	
	public String clean(String value) throws EncodingException, IntrusionException{
		String temp = ESAPIEncoder.getEncoder().canonicalize(value);
		return ESAPIEncoder.getEncoder().encodeForURL(temp);
	}
}
