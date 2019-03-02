package com.acme.commerce.security.xss;

import java.io.Serializable;

import org.owasp.esapi.errors.EncodingException;
import org.owasp.esapi.errors.IntrusionException;

/**
 * @author cgordon
 *
 */
public interface CleanerInterface extends Serializable{
	public static int EXCEPT_FORM_TYPE = 1;
	public static int EXCEPT_URL_TYPE = 2;
	public String clean(String uri) throws EncodingException, IntrusionException;
}
